package com.buzzinate.advertise.up

import scala.collection.mutable.HashMap

import org.apache.commons.lang.StringUtils

import dispatch.Request.toHandlerVerbs
import dispatch.Request.toRequestVerbs
import dispatch.Http
import dispatch.thread
import dispatch.url

case class UploadedFile(url: String, info: Map[String, Set[String]])

class UpYunClient(bucketname: String, username: String, rawPassword: String) {
  val http = new Http with thread.Safety
  http.client.getParams().setIntParameter("http.socket.timeout", 20000)
  http.client.getParams().setIntParameter("http.connection.timeout", 30000)
  
  val password = UpYunUtil.md5(rawPassword)
  val api_domain = "v0.api.upyun.com"
    
  def upload(dir: String, filename: String, data: Array[Byte], auto: Boolean): UploadedFile = {
    val headers = new HashMap[String, String]
    val date = UpYunUtil.getGMTDate
    headers += "Date" -> date
    headers += "Content-Md5" -> UpYunUtil.md5(data)
    if(auto) headers += "mkdir" -> "true"
    val filepath = "/" + StringUtils.replaceChars(dir, '.', '_') + filename
    val sign = UpYunUtil.sign("POST", "/" + bucketname + filepath, date, data.length, username, password)
    headers += "Authorization" -> sign
    val rh = http(((url("http://" + api_domain + "/" + bucketname + filepath)) << data <:< headers.toMap) >:> (x => x))
    UploadedFile("http://" + bucketname + ".b0.upaiyun.com" + filepath, rh)
  }
  
  def close(): Unit = http.shutdown
}