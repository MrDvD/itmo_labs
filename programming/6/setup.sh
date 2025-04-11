# build maven project
mvn archetype:generate -DgroupId=com.itmo.mrdvd -DartifactId=5 -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false

# add plugins to pom.xml

# <plugin>
#    <artifactId>maven-assembly-plugin</artifactId>
#    <configuration>
#       <archive>
#          <manifest>
#             <mainClass>com.itmo.mrdvd.Main</mainClass>
#          </manifest>
#       </archive>
#       <descriptorRefs>
#          <descriptorRef>jar-with-dependencies</descriptorRef>
#       </descriptorRefs>
#    </configuration>
# </plugin>
# <plugin>
#    <groupId>com.spotify.fmt</groupId>
#    <artifactId>fmt-maven-plugin</artifactId>
#    <version>2.25</version>
#    <executions>
#       <execution>
#          <goals>
#             <goal>format</goal>
#          </goals>
#       </execution>
#    </executions>
# </plugin>
# <plugin>

# add Apache Commons Lang module

# <dependency>
#   <groupId>org.apache.commons</groupId>
#   <artifactId>commons-lang3</artifactId>
#   <version>3.17.0</version>
# </dependency>

# add JacksonXML module

# <dependency>
#    <groupId>com.fasterxml.jackson.dataformat</groupId>
#    <artifactId>jackson-dataformat-xml</artifactId>
#    <version>2.11.1</version>
# </dependency>

# Jackson module for LocalDateTimeFormatter

# <dependency>
#    <groupId>com.fasterxml.jackson.datatype</groupId>
#    <artifactId>jackson-datatype-jsr310</artifactId>
#    <version>2.6.0</version>
# </dependency>

# Apache HttpCore module

# <dependency>
#     <groupId>org.apache.httpcomponents.core5</groupId>
#     <artifactId>httpcore5-h2</artifactId>
#     <version>5.3.4</version>
# </dependency>

# compile maven project to single JAR file
# the result will be placed into the target/ dir
mvn clean fmt:format compile assembly:single
