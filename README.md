# FeatJAR | formula-analysis-sat4j

## How to Use

### As an Executable

```
# count feature model solutions
java -jar formula-analysis-sat4j/build/libs/cli-*-all.jar --command countsat4j --input cli/src/test/resources/testFeatureModels/basic.xml
  
# or, equivalently, using Gradle
./gradlew :formula-analysis-sat4j:run --args " --command countsat4j --input src/test/resources/testFeatureModels/basic.xml"
./gradlew :formula-analysis-sat4j:run --args " --command coredead --input src/test/resources/testFeatureModels/basic.xml"
```

## Contributors

FeatJAR development team:

* [Thomas Thüm](https://www.uni-ulm.de/in/sp/team/thuem/) (University of Ulm, Germany)
* [Sebastian Krieter](https://www.uni-ulm.de/in/sp/team/sebastian-krieter/) (University of Ulm, Germany)
* [Elias Kuiter](https://www.dbse.ovgu.de/Mitarbeiter/Elias+Kuiter.html) (University of Magdeburg, Germany)

Further contributors and former project members:

* Daniel Hohmann (University of Magdeburg, Germany)
