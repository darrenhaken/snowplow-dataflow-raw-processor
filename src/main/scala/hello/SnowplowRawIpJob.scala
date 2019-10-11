package hello

import com.snowplowanalytics.snowplow.enrich.common.loaders.{IpAddressExtractor, ThriftLoader}
import com.spotify.scio.ScioContext
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.options._

object SnowplowRawIpJob {

  trait Options extends PipelineOptions with StreamingOptions {
    @Description("The Cloud Pub/Sub subscription to read from")
    //    @Required
    def getInputSubscription: ValueProvider[String]

    def setInputSubscription(value: ValueProvider[String]): Unit

    @Description("The Cloud Pub/Sub topic to write to")
    //    @Required
    def getOutputTopic: ValueProvider[String]

    def setOutputTopic(value: ValueProvider[String]): Unit
  }

  def main(cmdlineArgs: Array[String]): Unit = {
    PipelineOptionsFactory.register(classOf[Options])
    val options = PipelineOptionsFactory
      .fromArgs(cmdlineArgs: _*)
      .withValidation
      .as(classOf[Options])
    options.setStreaming(true)
    run(options)
  }

  def run(options: Options): Unit = {
    val sc = ScioContext(options)
    val subscription = "INSERT_SUBSCRIPTION_HERE"
    val inputIO = PubsubIO.readMessages().fromSubscription(subscription)

    sc
      .customInput("Snowplow input stream", inputIO)
      //      .debug()
      .map(binaryData => {
        val result = ThriftLoader.toCollectorPayload(binaryData.getPayload)
        print(result)
      })
      .debug()
    sc.close().waitUntilFinish()
  }
}
