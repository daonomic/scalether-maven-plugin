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
                <truffle>src/main/truffle/TestSaleContract.json</truffle>
                <packageName>io.daonomic.test.contracts.sale</packageName>
                <type>MONO</type>
            </contract>
            <contract>
                <abi>src/main/abi/TestTokenContract.json</abi>
                <packageName>io.daonomic.test.contracts.token</packageName>
                <type>MONO</type>
            </contract>
        </contracts>
    </configuration>
</plugin>

```

ABI json files and Truffle output files are supported.
