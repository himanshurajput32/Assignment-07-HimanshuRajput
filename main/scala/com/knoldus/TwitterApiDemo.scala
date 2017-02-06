package com.knoldus

import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Query, Twitter, TwitterFactory}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class MyTweets(getText: String, getUser: String, date: Date, likeCount: Int, retweetCount: Int)

/*
Trait for creating connection with twitter
 */
trait Config {

  def config(): Twitter = {
    val consumerKey = "================="
    val consumerSecretKey = "===================="
    val accessToken = "=============================="
    val accessTokenSecret = "=========="

    val configurationBuilder = new ConfigurationBuilder()
    configurationBuilder.setDebugEnabled(false)
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecretKey)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)
    val twitter: Twitter = new TwitterFactory(configurationBuilder.build()).getInstance()
    twitter
  }
}

class TwitterApiDemo extends Config {

  /*
  Method for retriving  tweets
   */
  def getTweets(num: Int, input: String): Future[List[MyTweets]] = Future {
    val twitter = config()
    val query = new Query(input)
    query.setCount(num)
    val list = twitter.search(query)
    val tweets = list.getTweets.asScala.toList
    val allTweets = tweets.map {
      tweet =>
        MyTweets(tweet.getText, tweet.getUser.getScreenName, tweet.getCreatedAt, tweet.getFavoriteCount, tweet.getRetweetCount)
    }
    allTweets.sortBy(_.date)
  }

  //Method to find average like and retweet per tweet
  def getAverageLikeRetweet(input: String, num: Int): Future[List[Int]] = Future {
    // val list = new ListBuffer[Int]
    val twitter = config()
    val query = new Query(input)
    query.setCount(num)
    val list = twitter.search(query)
    val tweets = list.getTweets.asScala.toList
    val allTweetLikeRetweet = tweets.map {
      tweet => (tweet.getFavoriteCount + tweet.getRetweetCount) / 2
    }
    allTweetLikeRetweet
  }
}

