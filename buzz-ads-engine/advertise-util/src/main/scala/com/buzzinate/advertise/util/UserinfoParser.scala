package com.buzzinate.advertise.util
import com.alibaba.fastjson.JSON
import scala.collection.mutable.ListBuffer

object UserinfoParser {

  case class UserInfo(categories: List[Integer])

  def parse(text: String): UserInfo = {
    if (text == null || text.isEmpty) {
      return new UserInfo(List[Integer]())
    }
    val json = JSON.parseObject(text)
    val categoriesStr = json.getString("category")
    var i = 0
    val cats = ListBuffer[Integer]()
    if (!org.apache.commons.lang.StringUtils.isEmpty(categoriesStr)) {
      i = 0
      val categoryArr = JSON.parseArray(categoriesStr)
      while (i < categoryArr.size) {
        val category = JSON.parseObject(categoryArr.getString(i))
        val cat = category.getInteger("cat")
        cats.append(cat)
        i += 1
      }
    }
    new UserInfo(cats.toList)
  }

}