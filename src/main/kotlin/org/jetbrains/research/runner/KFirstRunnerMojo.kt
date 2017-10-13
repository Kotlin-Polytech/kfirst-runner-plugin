package org.jetbrains.research.runner

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject

@Mojo(name = "run")
class KFirstRunnerMojo : AbstractMojo() {

    @Parameter
    private var packages: List<String> = emptyList()
    @Parameter
    private var authorFile: String = "author.name"
    @Parameter
    private var ownerFile: String = "owner.name"
    @Parameter
    private var resultFile: String = "results.json"
    @Parameter
    private var timeout: Long = 50L
    @Parameter
    private var sendToGoogle: Boolean = false

    @Parameter(defaultValue = "\${project}", readonly = true)
    private lateinit var project: MavenProject

    override fun execute() {
        val runner = org.jetbrains.research.runner.KFirstRunner()

        val args = RunnerArgs(
                projectDir = "",
                classpathPrefix = project.testClasspathElements.map { "file:$it/" },
                packages = packages,
                authorFile = authorFile,
                ownerFile = ownerFile,
                resultFile = resultFile,
                timeout = timeout,
                sendToGoogle = sendToGoogle
        )

        runner.run(args)
    }
}
