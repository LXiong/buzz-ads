package com.buzzinate.advertise.test.hbase
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HTablePool
import org.apache.hadoop.hbase.client.HTableInterface
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes
import com.buzzinate.advertise.test.server.Servers

object TestHbase {
  def main(args: Array[String]): Unit = {
    println("in")
    val cookieId = Servers.cookieId
    println(cookieId)
    val g = new Get(Bytes.toBytes(cookieId))
    println("done g")
    val r = Servers.table.get(g)
    println("done r")
    val data = Bytes.toString(r.getValue(Bytes.toBytes("data"), Bytes.toBytes("1")))
    println(data)
  }
}