# ${project.name}
![Maven Central Version](https://img.shields.io/maven-central/v/org.liveontologies/${project.artifactId})
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build status](https://ci.appveyor.com/api/projects/status/nd4uyg7hu88h0o2q?svg=true)](https://ci.appveyor.com/project/ykazakov/${project.artifactId})

${project.description}

For further information, see <${project.scm.url}>. 

## Prerequisites

${project.name} is tested to work with Protégé ${protege.version}. It may work 
with other versions of Protégé.

## Installation

To install, place all jar files inside the archive 

	${plugin-zip.file}.zip 

into the `plugins` folder of the Protege installation.

The plug-in supports Protege Auto Update feature which can be used for
upgrading to newer versions according to the instructions here:

    http://protegewiki.stanford.edu/wiki/EnablePluginAutoUpdate

## Development

To develop extensions to be used with this plugin, use the following Maven dependency:

```
<dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>${project.artifactId}</artifactId>
  <version>${releasedVersion.version}</version>
</dependency>
```

Each extension  should be a plug-in that implements the new 
extension points specified in
[`src/main/resources/plugin.xml`](${project.scm.url}/blob/main/src/main/resources/plugin.xml?raw=true).

See [Plugin Anatomy](https://protegewiki.stanford.edu/wiki/PluginAnatomy) for general
information about developing Protégé plugins.

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

${project.name} is Copyright (c) ${project.inceptionYear} - ${currentYear} ${project.organization.name}

All sources of this project are available under the terms of the 
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
(see the file `LICENSE.txt`).