import de.undercouch.gradle.tasks.download.Download
import net.sourceforge.plantuml.*
import net.sourceforge.plantuml.sprite.SpriteGrayLevel
import net.sourceforge.plantuml.sprite.SpriteUtils
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.tools.ant.filters.ReplaceTokens
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import javax.imageio.ImageIO

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.41"
    id("de.undercouch.download") version "4.0.1"
}

repositories {
    jcenter()
}

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("net.sourceforge.plantuml:plantuml:1.2019.9")
        classpath("org.apache.xmlgraphics:batik-all:1.10")
    }
}

val zipFile = File(buildDir, "icons.zip")
task("downloadFirebaseIcon", Download::class) {
    src("https://firebase.google.com/downloads/brand-guidelines/firebase-product-branding.zip")
    dest(zipFile)
    overwrite(false)
}

task("copyIcons") {
    dependsOn("downloadFirebaseIcon")
    doLast {
        copy {
            from(zipTree(zipFile).matching {
                include("**/*(4- Icon, Use in Firebase Contexts Only).svg")
            })
            filter(ReplaceTokens::class, "beginToken" to "<g fill=\"", "endToken" to "\">", "tokens" to mapOf("none" to "<g fill=\"#ffffff\">"))
            filter(ReplaceTokens::class, "beginToken" to "\"", "endToken" to "\"", "tokens" to mapOf("#ffca28" to "\"#000000\""))
            filter(ReplaceTokens::class, "beginToken" to "\"", "endToken" to "\"", "tokens" to mapOf("#ff8a65" to "\"#000000\""))
            filter(ReplaceTokens::class, "beginToken" to "\"", "endToken" to "\"", "tokens" to mapOf("#ffa000" to "\"#000000\""))
            rename { filename ->
                filename
                        .replace("4- Icon, Use in Firebase Contexts Only", "")
                        .replace("()", "")
                        .replace(" for ", "")
                        .replace("Cloud", "")
                        .replace("Firebase", "")
                        .replace("Google", "")
                        .replace("-", "")
                        .replace(" ", "")
            }
            into(File("plantuml"))
        }
    }
}

val generatePuml = fileTree("plantuml").apply {
    include("**/*.puml")
    exclude("FirebaseCommon.puml")
}

task("cleanIcons", Delete::class) {
    delete(
            fileTree("plantuml").include("**/*.svg"),
            fileTree("plantuml").include("**/*.png"),
            generatePuml)
}
tasks["clean"].dependsOn("cleanIcons")

task("buildSvg2Png", Svg2PngTask::class) {
    dependsOn("copyIcons")
    inputFiles = fileTree("plantuml").apply {
        include("**/*.svg")
    }
    outputWidth = 64f
    outputHeight = 64f
}

task("buildSprite", BuildSpriteTask::class) {
    dependsOn("buildSvg2Png")
    inputFiles = fileTree("plantuml").apply {
        include("**/*.png")
    }
}

task("buildAllSprite") {
    dependsOn("buildSprite")
    val outputFile = File("plantuml/FirebaseAll.puml")
    outputFile.createNewFile()
    val output = PrintWriter(outputFile.outputStream())
    generatePuml.files.forEach() {
        output.println(it.readText())
    }
    output.flush()
    output.close()
}

task("buildAssets") {
    dependsOn("buildAllSprite")
    val outDir = File(rootDir,"assets")
    doLast {
        fileTree("examples").apply {
            include("**/*.puml")
        }.visit {
            if (file.isDirectory) {
                return@visit
            }
            SourceFileReader(
                    file,
                    File(outDir, relativePath.parent.pathString),
                    FileFormatOption(FileFormat.SVG)
            ).generatedImages
        }
    }
}

tasks["build"].dependsOn("buildAllSprite", "buildAssets")

open class Svg2PngTask : DefaultTask() {
    @InputFiles
    open lateinit var inputFiles: FileTree
    open var outputWidth: Float = 512f
    open var outputHeight: Float = 512f

    @TaskAction
    fun exec() {
        inputFiles.files.forEach { target ->
            val outputFile = File(target.parentFile.absolutePath,"${target.absoluteFile.nameWithoutExtension}.png")
            outputFile.createNewFile()
            val input = target.absoluteFile.inputStream()
            val output = outputFile.outputStream()
            try {
                convert(input, output, outputWidth, outputHeight)
            } finally {
                input.close()
                output.close()
            }
        }
    }

    private fun convert(`in`: InputStream, out: OutputStream, width: Float, height: Float) {
        val input = TranscoderInput(`in`)
        val output = TranscoderOutput(out)
        val pngTranscoder = PNGTranscoder()

        pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width)
        pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height)
        pngTranscoder.createImage(width.toInt(), height.toInt())

        pngTranscoder.transcode(input, output)
    }
}

open class BuildSpriteTask : DefaultTask() {
    @InputFiles
    open lateinit var inputFiles: FileTree

    val colorPatterns = mapOf(
            "Firestore" to "FIREBASE_YELLOW",
            "MLKit" to "FIREBASE_YELLOW",
            "Functions" to "FIREBASE_YELLOW",
            "Authentication" to "FIREBASE_YELLOW",
            "Hosting" to "FIREBASE_YELLOW",
            "Storage" to "FIREBASE_YELLOW",
            "RealtimeDatabase" to "FIREBASE_YELLOW",

            "Crashlytics" to "FIREBASE_AMBER",
            "AppDistribution" to "FIREBASE_AMBER",
            "PerformanceMonitoring" to "FIREBASE_AMBER",
            "TestLab" to "FIREBASE_AMBER"
    )

    @TaskAction
    fun exec() {
        inputFiles.files.forEach { target ->
            val outputFile = File(target.parentFile.absolutePath,"${target.absoluteFile.nameWithoutExtension}.puml")
            outputFile.createNewFile()
            val input = target.absoluteFile.inputStream()
            val output = outputFile.outputStream()
            try {
                convert(target.absoluteFile.nameWithoutExtension, input, output)
            } finally {
                input.close()
                output.close()
            }
        }
    }

    private fun convert(name: String, `in`: InputStream, out: OutputStream) {
        val input = ImageIO.read(`in`)
        val output = PrintWriter(out)
        val sprite = SpriteUtils.encodeCompressed(input, name, SpriteGrayLevel.GRAY_16)
        output.println(sprite.replace("\\r\\n|\\r", "\n"))
        output.println()
        output.println("FirebaseEntityColoring($name)")
        val color = colorPatterns[name]?: "FIREBASE_CORAL"
        output.println("!define $name(alias, label, part_desc) FirebaseEntity(alias, label, part_desc, $color, $name, $name)")
        output.println("!define ${name}Participant(alias, label, part_desc) FirebaseParticipant(alias, label, part_desc, $color, $name, $name)")
        output.flush()
    }
}