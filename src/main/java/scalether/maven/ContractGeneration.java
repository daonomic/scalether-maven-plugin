package scalether.maven;

import scalether.generator.domain.Type;

import java.io.File;

public class ContractGeneration {
    private String name;
    private File abi;
    private File truffle;
    private String packageName;
    private Type type;
    private boolean test;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getAbi() {
        return abi;
    }

    public void setAbi(File abi) {
        this.abi = abi;
    }

    public File getTruffle() {
        return truffle;
    }

    public void setTruffle(File truffle) {
        this.truffle = truffle;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
