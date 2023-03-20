(You came here via the "factory entrance". Unless you are highly computer literate, you are better off skipping to the **[wiki](https://github.com/Papa-26/GS_LV/wiki)**.)
## The GS_MV Project
This repository serves the distribution, maintenance, support, and further development of G_String_M, an upgraded version of [G_String_L](https://github.com/G-String-Legacy/G_String), a software tool allowing generalists to perform [Generalizability Analysis](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC6699529/#:~:text=Generalizability%20studies%20provide%20a%20better,within%20the%20universe%20of%20scores.) of performance evaluations.
In contrast to more general statistical tools, G_String does not require a great deal of mathematical sophistication. It guides the user step-by-step through the accepted mathematical algorithms.
Until the present, G_String was programmed in traditional Oracle Java 8. But over the past 15 years the software industry has made great progress.
**GS_M** is an upgrade that runs on any Windows, Mac, or Linux (Debian) without specific library requirements. While it currently uses Java and JavaFX version-17 automatically, it is easily upgradable to future versions, thus keeping step with further development.
The key to this development was to incorporate G_String with its own build tool - Gradle.

So what does the **M** in **GS_M** stand for?
- **M** for **Modular**: old G_String was coded entirely in Oracle Java version 8, which had an overly simple structure, and therefore was more susceptible to problems. Since Java 9, Oracle has introduced a modular program structure. **GS_M** is coded accordingly.
- **M** for **Modern**: Java 8 was strictly an Oracle product that also included graphic libraries. From version 9 on, the graphics libraries (javafx) became a separate product, and the market split further between official, commercially licensed Oracle products, and a variety of Open Source software. So each installation of an operating system typically can have different combinations of java and javafx versions installed. As a consequence, installing non-modular graphical applications has become a nightmare. Modern G_String **GS_M** carries the required libraries already in the installation package.
- **M** for **Mega**: as a consequence **GS_M** is much bigger than **GS_L**, typically closer to 100 MB.

In other words **GS_M** is more stable, easier to install, easier to maintain, and easier to adapt to changes in the digital ecosystem than old **GS_L**.

Early releases for Linux, Windows and macOS can now be [downloaded here](https://github.com/Papa-26/GS_LV/releases).
