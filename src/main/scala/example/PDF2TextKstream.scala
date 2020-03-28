package example

import java.util.Properties

import com.google.gson.Gson
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.pdfbox.pdmodel.{PDDocument, PDDocumentInformation}
import org.apache.pdfbox.text.PDFTextStripper

case class PDF(meta:Map[String, String], text:String)

object PDF2TextKstream extends App {

  val builder = new StreamsBuilder()

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "pdf-text")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put("schema.registry.url", "localhost:8081")
    p
  }

  val pdfs: KStream[String, Array[Byte]] = builder.stream[String, Array[Byte]]("pdf")

  val branches = pdfs.map((k,v) => {

    try{
      val document: PDDocument = PDDocument.load(v)
      val metadata = document.getDocumentInformation()
      val md = Map(
        "author" -> metadata.getAuthor,
        "creator" -> metadata.getCreator,
        "keys" -> metadata.getKeywords,
        "producer" -> metadata.getProducer,
        "subject" -> metadata.getSubject,
        "title" -> metadata.getTitle
      )

      val pdfStripper = new PDFTextStripper()
      val text = pdfStripper.getText(document)
      document.close()
      (k, PDF(meta=md,text=text))
    } catch {
      case _: Throwable => {
        print(_)
        (k, PDF(null, null))
      }
    }

  }).branch( // route documents
    (k, v) => v.text == null, // if text is null
    (k, v) => v.text.toLowerCase().contains("kafka"), // if text contains the word kafka
    (k, v) => true //
  )

  branches(0).map((k, v) => {
    val gson = new Gson()
    val json = gson.toJson(v)
    (k, json)
  }).to("pdf-json-0")

  branches(1).map((k, v) => {
    val gson = new Gson()
    (k, gson.toJson(v))
  }).to("pdf-json-1")

  branches(2).map((k, v) => {
    val gson = new Gson()
    (k, gson.toJson(v))
  }).to("pdf-json-2")

  val topology = builder.build()
  val streams: KafkaStreams = new KafkaStreams(topology, config)
  println(topology.describe())
  streams.start()
}
