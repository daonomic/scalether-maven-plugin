package scalether.maven;

import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import scalether.generator.ContractGenerator;
import scalether.generator.domain.AbiItem;
import scalether.generator.domain.TruffleContract;
import scalether.generator.domain.Type;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenMojo extends AbstractMojo {

    /**
     * The project being built.
     */
    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    /**
     * Location of the output directory.
     */
    @Parameter(name = "output", defaultValue = "${project.build.directory}/generated-sources/scalether")
    private File output;

    @Parameter(name = "contracts")
    private List<ContractGeneration> contracts;

    public void execute() throws MojoExecutionException {
        for (ContractGeneration contract : this.contracts) {
            try {
                if (contract.getTruffle() != null) {
                    getLog().info("Generating contract wrapper by truffle for " + contract.getTruffle().getName());
                    generateByTruffle(contract.getName(), contract.getTruffle(), contract.getPackageName(), contract.getType());
                } else if (contract.getAbi() != null) {
                    if (StringUtils.isBlank(contract.getName())) {
                        getLog().warn("name not specified for abi " + contract.getAbi().getName());
                    } else {
                        getLog().info("Generating contract wrapper by abi for " + contract.getAbi().getName());
                        generateByAbi(contract.getName(), contract.getAbi(), contract.getPackageName(), contract.getType());
                    }
                } else {
                    getLog().warn("no abi or truffle specified for contract generation. ignoring");
                }
            } catch (Exception e) {
                throw new MojoExecutionException("unable to generate contract wrapper", e);
            }
        }

        project.addCompileSourceRoot(output.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void generateByTruffle(String name, File truffleFile, String packageName, Type type) throws IOException, TemplateException {
        ContractGenerator generator = new ContractGenerator();
        TruffleContract truffle;
        try (InputStream in = new FileInputStream(truffleFile)) {
            truffle = generator.mapper.readValue(in, TruffleContract.class);
        }
        if (StringUtils.isNotBlank(name)) {
            truffle.setName(name.trim());
        }
        String resultPath = output.getAbsolutePath() + "/" + packageName.replace(".", "/");
        new File(resultPath).mkdirs();
        try (OutputStream out = new FileOutputStream(new File(resultPath + "/" + truffle.getName() + ".scala"))) {
            generator.generate(out, truffle, packageName, type != null ? type : Type.SCALA);
        }

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void generateByAbi(String name, File abiFile, String packageName, Type type) throws IOException, TemplateException {
        ContractGenerator generator = new ContractGenerator();
        AbiItem[] abi;
        try (InputStream in = new FileInputStream(abiFile)) {
            abi = generator.mapper.readValue(in, AbiItem[].class);
        }
        TruffleContract truffle = new TruffleContract(name, Arrays.asList(abi), "0x");
        String resultPath = output.getAbsolutePath() + "/" + packageName.replace(".", "/");
        new File(resultPath).mkdirs();
        try (OutputStream out = new FileOutputStream(new File(resultPath + "/" + truffle.getName() + ".scala"))) {
            generator.generate(out, truffle, packageName, type != null ? type : Type.SCALA);
        }
    }
}
