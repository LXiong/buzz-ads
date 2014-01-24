package com.buzzinate.advertise.tool

import java.io.ByteArrayOutputStream
import java.io.File

import org.apache.commons.lang.StringUtils

import com.buzzinate.advertise.batch.BatchContent
import com.buzzinate.advertise.crawl.Thumbnail
import com.mortennobel.imagescaling.AdvancedResizeOp
import com.mortennobel.imagescaling.ResampleOp

import javax.imageio.ImageIO


object CreatThumbnail {
    def updateThumbnail(imgFile: File, url: String): String = {
      val img = ImageIO.read(imgFile)
      val format = StringUtils.substringAfter(imgFile.getName, ".")
      val resampleOp = new ResampleOp(100, 100)
      resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal)
      val small = resampleOp.filter(img, null)
      val baos = new ByteArrayOutputStream
      ImageIO.write(small, format, baos)
      val thumUrl = BatchContent.upload(url, Thumbnail(imgFile.getName(), format, baos.toByteArray))
      thumUrl
    }
    
    def main(args: Array[String]): Unit = {
      val thumUrl = updateThumbnail(new File("updateAds/ad1/ad1.jpg"), "http://c03.optimix.asia/imageviews?opxCREATIVEID=3245&opxPLACEMENTID=763&opxCREATIVEASSETID=13501&opxMODE=1&opxURL=http://www.bole.com/zt/jhzt/")
      println(thumUrl)
    }
}