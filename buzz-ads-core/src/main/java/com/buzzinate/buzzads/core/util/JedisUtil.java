package com.buzzinate.buzzads.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.common.base.Function;

/**
 * A set of utility methods that makes it more convenient to work with
 * <a href="https://github.com/xetorthio/jedis">Jedis</a>.
 * 
 * @author Wiehann Matthysen
 */
public final class JedisUtil {
    private static Log log = LogFactory.getLog(JedisUtil.class);
    
    private JedisUtil() { }

    /**
     * Wraps the given {@link JedisPool} with extra logic to properly destroy
     * the pool of connections when the virtual-machine is shutdown.
     * @param pool The {@code JedisPool} of connections for which this extra
     * logic needs to be added.
     * @return The resulting {@code JedisPool} that will be properly destroyed
     * upon virtual-machine shutdown.
     */
    public static ShardedJedisPool destroyOnShutdown(final ShardedJedisPool pool) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                pool.destroy();
            }
        });
        return pool;
    }

    /**
     * @see JedisUtil#using(redis.clients.jedis.JedisPool)
     */
    interface LinkedCallbackBuilder {
        <T> T call(Function<ShardedJedis, T> callback);
    }

    /**
     * A convenience method that provides for automatic resource management of
     * Jedis connections. This method will be deprecated once Jedis incorporates
     * Java 7's automatic resource management.
     * 
     * Calling this method yields a {@link LinkedCallbackBuilder}, which
     * opens up to two more method calls in the DSL named
     * {@link LinkedCallbackBuilder#_do(fj.F)} and
     * {@link LinkedCallbackBuilder#_do(fj.Effect)}.
     * 
     * <p>
     * The first takes an object of type {@code F<Jedis, T>} as parameter.
     * {@code F<Jedis, T>} is basically a callback (or closure) that receives as
     * input a {@link Jedis} connection from the given {@link JedisPool} and
     * returns a value of type {@code T}. If no errors occurred this value will
     * be returned to the calling code-block.
     * </p>
     * 
     * <p>
     * The second call takes an object of type {@code Effect<Jedis>} as
     * parameter. {@code Effect<Jedis>} is a callback (or closure) that receives
     * as input a {@code Jedis} connection from the given {@code JedisPool}, but
     * returns no value to the calling code-block.
     * </p>
     * 
     * Both of the abovementioned method-calls can be provided with in-line
     * anonymous class implementations of {@code F<Jedis, T>} or
     * {@code Effect<Jedis>} as the following example illustrates:
     * 
     * <pre>
     * import static org.strawberry.util.JedisUtil.using;
     * 
     * ...
     * 
     * JedisPool pool = new JedisPool("localhost", 6379);
     * using(pool)._do(new F&lt;Jedis, String&gt;() {
     * 
     *   {@literal @}Override
     *   public String f(Jedis jedis) {
     *     // load string-value from redis.
     *     ...
     *     return stringValue;
     *   }
     * });
     * 
     * using(pool)._do(new Effect&lt;Jedis&gt;() {
     * 
     *   {@literal @}Override
     *   public void e(Jedis jedis) {
     *     //do something in redis.
     *   }
     * });
     * </pre>
     * 
     * 
     * This {@code Jedis} connection will be returned to the {@code JedisPool}
     * at the end of the {@code F<Jedis, T>} or {@code Effect<Jedis>} callback
     * method. Also; if something went wrong with the {@code Jedis} connection
     * itself, or an exception was raised inside the callback; the connection
     * will be correctly returned to the {@code JedisPool} before raising the
     * exception to be handled by the calling code-block.
     * 
     * @param <T> The parameterizing type for the subsequent
     * {@code _do(F<Jedis, T>...)} method call in the DSL call-chain.
     * @param pool The pool of {@code Jedis} connections to be utilized during
     * the callback step.
     * @throws JedisConnectionException if an error occurred while sending or
     * receiving data from a {@code Jedis} connection in the {@code JedisPool}
     * of connections.
     * @return The {@code LinkedCallbackBuilder} serving as bridge to the two
     * {@code _do(...)} calls.
     */
    public static LinkedCallbackBuilder using(final ShardedJedisPool pool) {
        return new LinkedCallbackBuilder() {

            @Override
            public <T> T call(Function<ShardedJedis, T> callback) {
                T result = null;
                boolean returned = false;
                ShardedJedis jedis = pool.getResource();
                try {
                    result = callback.apply(jedis);
                } catch (JedisConnectionException e) {
                    pool.returnBrokenResource(jedis);
                    returned = true;
                    log.fatal(e);
                } finally {
                    if (!returned) pool.returnResource(jedis);
                }
                return result;
            }
        };
    }
}