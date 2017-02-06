package com.knoldus

import org.scalatest.FunSuite

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class TwitterApiDemoTest extends FunSuite {
  val obj = new TwitterApiDemo
  test("Tweet Count") {
    assert(Await.result(obj.getTweets(100, "#Modi"), Duration.Inf).length != 1)
  }
  test("Average Like Retweet Count") {
    assert(Await.result(obj.getAverageLikeRetweet("#Modi", 100), Duration.Inf).length != 1)
  }
  test("No Result") {
    assert(Await.result(obj.getTweets(100, "#Modi"), Duration.Inf) != Nil)
  }
}
