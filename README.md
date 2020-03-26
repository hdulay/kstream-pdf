# KStream PDF parser

This example uses KStreams to read a topic containing PDF documents in binary format. The KStreams application will parse the PDF using *org.apache.pdfbox* library and extract the text. The text and some metadata is routed to different topics based on content of the PDF.

![Diagram](diagram.png)