# Generate smart contract wrappers and use them to call/execute transactions

## add scalether-maven-plugin to pom.xml

```xml
<plugin>
    <groupId>io.daonomic.scalether</groupId>
    <artifactId>scalether-maven-plugin</artifactId>
    <version>${scalether.version}</version>
    <executions>
      <execution>
        <phase>generate-sources</phase>
        <goals>
          <goal>generate</goal>
        </goals>
      </execution>
    </executions>                
    <configuration>
        <contracts>
            <!--list all contracts you want-->
            <contract>
                <!--this is file from build/contracts folder from Truffle. If contract is not abstract, you will be able to deploy contract -->
                <truffle>src/main/truffle/TestSaleContract.json</truffle>
                <packageName>io.daonomic.test.contracts.sale</packageName>
                <!--Specify MONO if you want to generate contracts for project reactor's Mono-->
                <type>MONO</type>
            </contract>
            <contract>
                <!--If you specify abi json file, no contract deployment will be possible, only wrapper is generated-->
                <abi>src/main/abi/TestTokenContract.json</abi>
                <packageName>io.daonomic.test.contracts.token</packageName>
                <type>MONO</type>
            </contract>
        </contracts>
    </configuration>
</plugin>

```

ABI json files and Truffle output files are supported.
