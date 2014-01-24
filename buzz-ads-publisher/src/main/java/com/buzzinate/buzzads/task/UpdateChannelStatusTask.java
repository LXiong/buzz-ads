package com.buzzinate.buzzads.task;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.core.dao.ChannelDao;
import com.buzzinate.buzzads.core.dao.StatsChannelDailyDao;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.Channel;
import com.buzzinate.buzzads.enums.ChannelStatusEnum;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.task.AbstractSingleServerTask;
/**
 * 定时更新媒体的状态
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2013-5-16
 */
public class UpdateChannelStatusTask extends AbstractSingleServerTask {
    private static final Log LOGGER = LogFactory.getLog(UpdateChannelStatusTask.class);
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private StatsChannelDailyDao channelDailyDao;

    public UpdateChannelStatusTask() {
        //定时任务运行主机
        super(ConfigurationReader.getString("admax.channel.status.host"));
    }

    @Override
    protected void doJob() {
        Executor executor = Executors.newFixedThreadPool(2);
        //开启
        executor.execute(new OpenChannelThread("Open channel thread"));
        //关闭
        executor.execute(new CloseChannelThread("Close channel thread"));

    }
    /**
     * 关闭媒体线程
     * @author Harry Feng<xiaobo.feng@buzzinate.com>
     *
     * 2013-5-16
     */
    class CloseChannelThread implements Runnable {

        public CloseChannelThread(String threadName) {
            Thread.currentThread().setName(threadName);
        }
        
        @Override
        public void run() {
            closeChannel();
        }
        
    }
    /**
     * 开启媒体线程
     * @author Harry Feng<xiaobo.feng@buzzinate.com>
     *
     * 2013-5-16
     */
    class OpenChannelThread implements Runnable {

        public OpenChannelThread(String threadName) {
            Thread.currentThread().setName(threadName);
        }
        @Override
        public void run() {
            openChannel();
        }
        
    }

    private void closeChannel() {
        boolean flag = true;
        Date yesterday = DateTimeUtil.getYestoday();
        while (flag) {
            //检索出100条开启状态的媒体
            List<Channel> openList = channelDao.findChannelByStatus(ChannelStatusEnum.OPENED, 100);
            List<Integer> closeIds = new LinkedList<Integer>();
            int size = openList.size();
            LOGGER.debug("Close channel size:" + size);
            if (size < 100)
                flag = false;
            for (Channel channel : openList) {
                if (channelDailyDao.getChannelDaily(channel.getId(), yesterday) == null) {
                    closeIds.add(Integer.valueOf(channel.getId()));
                }
            }
            if (closeIds.size() > 0) {
                channelDao.closeChannel(closeIds);
            }
        }
        LOGGER.debug("Close channel stop");
    }

    private void openChannel() {
        boolean flag = true;
        Date yesterday = DateTimeUtil.getYestoday();
        while (flag) {
            //检索出100条关闭状态的媒体
            List<Channel> closeList = channelDao.findChannelByStatus(ChannelStatusEnum.CLOSED, 100);
            List<Integer> openIds = new LinkedList<Integer>();
            int size = closeList.size();
            LOGGER.debug("Close channel size:" + size);
            if (size < 100)
                flag = false;
            for (Channel channel : closeList) {
                if (channelDailyDao.getChannelDaily(channel.getId(), yesterday) != null) {
                    openIds.add(Integer.valueOf(channel.getId()));
                }
            }
            if (openIds.size() > 0) {
                channelDao.openChannel(openIds);
            }
        }
        LOGGER.debug("Open channel stop");
    }

}
