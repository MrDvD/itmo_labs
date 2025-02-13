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

# compile maven project to single JAR file
# the result will be placed into the target/ dir
mvn clean fmt:format compile assembly:single