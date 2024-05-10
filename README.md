# Protege Justification Explanation
![GitHub Release](https://img.shields.io/github/v/release/liveontologies/protege-justification-explanation)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build status](https://ci.appveyor.com/api/projects/status/nd4uyg7hu88h0o2q?svg=true)](https://ci.appveyor.com/project/ykazakov/protege-justification-explanation)

A plug-in for the Protégé Desktop ontology editor that adds an explanation service for
displaying minimal subset of axioms (justifications) for entailments. Compared to the
explanation-workbench plug-in bundled with Protégé, this plug-in can use multiple
providers for computing justifications. Each of these providers should be a plug-in
that implements the new extension points (see plugin.xml).

For further information, see <https://github.com/liveontologies/protege-justification-explanation>. 

## Prerequisites

Protege Justification Explanation is tested to work with Protégé 5.5.0. It may work 
with other versions of Protégé.

## Installation

To install, place all jar files inside the archive 

	protege-justification-explanation-0.0.1-SNAPSHOT.zip 

into the `plugins` folder of the Protege installation.

The plug-in supports Protege Auto Update feature which can be used for
upgrading to newer versions according to the instructions here:

    http://protegewiki.stanford.edu/wiki/EnablePluginAutoUpdate

## Development

To develop extensions to be used with this plugin, use the following Maven dependency:

```
<dependency>
  <groupId>org.liveontologies</groupId>
  <artifactId>protege-justification-explanation</artifactId>
  <version>${releasedVersion.version}</version>
</dependency>
```
See [`src/main/resources/plugin.xml`](https://github.com/liveontologies/protege-justification-explanation/blob/main/src/main/resources/plugin.xml?raw=true) for the definition of the required extension-points.

To use snapshots versions of this library (if not compiled from sources), please add
the Sonatype OSSRH snapshot repository either to your `pom.xml` or `settings.xml`:
```
<repositories>
  <repository>
    <id>ossrh-snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

## License

Protege Justification Explanation is Copyright (c) 2016 - 2024 Live Ontologies Project

All sources of this project are available under the terms of the 
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
(see the file `LICENSE.txt`).