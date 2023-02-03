## The GS_M Project
This repository acts as a workshop for the development and testing of a future version of G_String, a software tool allowing generalists to perform Generalizability Analysis of performance evaluations.
In contrast to more general statistical tools, G_String does not require a great deal of mathematical sophistication. It guides the user through the accepted mathematical algorithms.
Until the present, G_String was programmed in traditional Oracle Java 8. But over the past 15 years the software industry has made great progress. 
The project **GS_M** is an attempt for G_String, not only to catch up but also, to hopefully, keep step with future development more easily.
The key to this development was to incorporate G_String with its own build tool - Gradle.

So what does the **M** in **GS_M** stand for?
- **M** for **Modular**: old G_String was coded entirely in Oracle Java version 8, which was somewhat deficient in regards to a systematic structure, and therefore more prone to bugs. Since Java 9, Oracle has introduced modular program structure. **GS_MV** is coded in modular form.
- **M** for **Modern**: Java 8 was strictly an Oracle product that also included graphic libraries. From version 9 on, the graphics libraries (javafx) became a separate product, and the market split further between official, commercially licensed Oracle products, and a variety of Open Sources. So each installation of an operating system typically can have different combinations of java and javafx versions installed. As a consequence, installing non-modular graphical applications has become a nightmare. Modern G_String **GS_M** therefore carries the required libraries already in the installation package.
- **M** for **Mega**: as a consequence **GS_M** is much bigger than **GS_L**, typically around 100 MB.


