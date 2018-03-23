/*  -*- mode: Scala; -*- */

import org.apache.spark.rpc.netty
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.hive.HiveContext
import scala.collection._
import scala.reflect.runtime.universe.TypeTag

val conf = new SparkConf().setAppName("ScratchPad").setMaster("local")
val sc = new SparkContext(conf)
val hc = new HiveContext(sc)
val sqlContext = hc

import sqlContext.implicits._

def dump(df: DataFrame)(implicit n: Int = 10): Unit = { df.printSchema; df.show }
def dsdump(ds: Dataset[_])(implicit n: Int = 10): Unit = { ds.printSchema; ds.show }
def ddump[A <: Product: TypeTag](dd: RDD[A])(implicit n: Int = 10): Unit = {
  val sqlContext = SQLContext.getOrCreate(dd.sparkContext)
  import sqlContext._
  import sqlContext.implicits._
  dump(sqlContext.createDataFrame[A](dd))(n)
}

val kvv = Array("foo=A", "foo=A", "foo=A", "foo=A", "foo=B", "bar=C", "bar=D", "bar=D")
val kdd = sc.parallelize(kvv)
//Create key value pairs
val kv = kdd.map(_.split("=")).map(v => (v(0), v(1))).cache()

val initialSet = mutable.HashSet.empty[String]
val addToSet = (s: mutable.HashSet[String], v: String) => s += v
val mergePartitionSets = (p1: mutable.HashSet[String], p2: mutable.HashSet[String]) => p1 ++= p2
val kdu = kv.aggregateByKey(initialSet)(addToSet, mergePartitionSets)

ddump(kv)