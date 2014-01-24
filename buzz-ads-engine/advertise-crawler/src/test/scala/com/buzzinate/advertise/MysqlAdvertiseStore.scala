package com.buzzinate.advertise

import java.sql.DriverManager

object MysqlAdvertiseStore {
  def main(args: Array[String]): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    val conn = DriverManager.getConnection(
      "jdbc:mysql://192.168.1.134:3306/buzzads", "ads", "123456")
    val stmt = conn.createStatement()
    val rs = stmt.executeQuery("select ad_entry.ID,ad_entry.UPDATE_AT, ad_entry.LINK, ad_entry.RESOURCE_URL, ad_entry.TITLE, ad_entry.RESOURCE_TYPE from ad_entry, ad_order where ad_entry.ORDER_ID = ad_order.ID and ad_order.STATUS = '1'")
    println("start")
    while (rs.next()) {
      println(rs.getString("ID"))
      println(rs.getString("TITLE"))
      println(rs.getString("LINK"))
      println(rs.getString("RESOURCE_URL"))
      println(rs.getString("RESOURCE_TYPE"))
      println(rs.getDate("UPDATE_AT"))
      println("----------------")
    }
    println("end")
    rs.close()
    stmt.close()
    conn.close()
  }
}