package org.jetbrains.research.runner

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.repository.RemoteRepository
import org.jetbrains.research.runner.util.getClasspathFromModel
import java.io.File

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

    @Parameter(defaultValue = "\${project}", readonly = true)
    private lateinit var project: MavenProject

    @Component
    private lateinit var repoSystem: RepositorySystem

    @Parameter(defaultValue = "\${repositorySystemSession}", readonly = true)
    private lateinit var repoSession: RepositorySystemSession

    @Parameter(defaultValue = "\${project.remoteProjectRepositories}", readonly = true)
    private lateinit var remoteRepos: List<RemoteRepository>

    override fun execute() {
        val runner = org.jetbrains.research.runner.KFirstRunner()

        val classpathFiles = project.testClasspathElements.map { File(it) } +
                getClasspathFromModel(project.model, repoSystem, repoSession, remoteRepos).map { it.artifact.file }

        val args = RunnerArgs(
                projectDir = "",
                classpathPrefix = classpathFiles.map {
                    it.toURI().toString()
                },
                packages = packages,
                authorFile = authorFile,
                ownerFile = ownerFile,
                resultFile = resultFile,
                timeout = timeout
        )

        runner.run(args)
    }
}
