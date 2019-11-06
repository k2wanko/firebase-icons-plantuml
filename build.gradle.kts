import de.undercouch.gradle.tasks.download.Download
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceFileReader
import net.sourceforge.plantuml.sprite.SpriteGrayLevel
import net.sourceforge.plantuml.sprite.SpriteUtils
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.tools.ant.filters.ReplaceTokens
import java.awt.*
import java.awt.image.*
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import javax.imageio.ImageIO
import java.util.regex.*




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
        val transformer: (String) -> String = { filename ->
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
        copy {
            from(zipTree(zipFile).matching {
                include("**/*(4- Icon, Use in Firebase Contexts Only).svg")
            })
            rename(transformer)
            into(File("plantuml"))
        }
        copy {
            from(zipTree(zipFile).matching {
                include("**/*(4- Icon, Use in Firebase Contexts Only).png")
            })
            rename(transformer)
            into(File(buildDir,"png"))
        }
        copy {
            from(fileTree("plantuml").matching {
                include("**/*.svg")
            })
            rename(transformer)
            filter(ReplaceTokens::class, "beginToken" to "<g fill=\"", "endToken" to "\">", "tokens" to mapOf("none" to "<g fill=\"#ffffff\">"))
            filter(ReplaceTokens::class, "beginToken" to "\"", "endToken" to "\"", "tokens" to mapOf("#ffca28" to "\"#000000\""))
            filter(ReplaceTokens::class, "beginToken" to "\"", "endToken" to "\"", "tokens" to mapOf("#ff8a65" to "\"#000000\""))
            filter(ReplaceTokens::class, "beginToken" to "\"", "endToken" to "\"", "tokens" to mapOf("#ffa000" to "\"#000000\""))
            into(File(buildDir, "sprite"))
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
            fileTree("assets"),
            fileTree("build"),
            generatePuml)
}
tasks["clean"].dependsOn("cleanIcons")

task("buildSvg2Png", Svg2PngTask::class) {
    dependsOn("copyIcons")
    inputFiles = fileTree(File(buildDir, "sprite")).matching {
        include("**/*.svg")
    }
    outputWidth = 512f
    outputHeight = 512f
}

task("resizeSpritePng", ResizePngTask::class) {
    dependsOn("buildSvg2Png")
    inputFiles = fileTree(File(buildDir, "sprite")).matching {
        include("**/*.png")
    }
    outputDirectory = File(buildDir, "sprite.2")
    outputWidth = 64
    outputHeight = 64
}

task("buildSprite", BuildSpriteTask::class) {
    dependsOn("resizeSpritePng")
    inputFiles = fileTree(File(buildDir, "sprite.2")).matching {
        include("**/*.png")
    }
    outputDirectory = File(rootDir, "plantuml")
}

task("resizePng", ResizePngTask::class) {
    dependsOn("copyIcons")
    inputFiles = fileTree(File(buildDir, "png")).matching {
        include("**/*.png")
    }
    outputDirectory = File(rootDir, "plantuml")

    outputWidth = 64
    outputHeight = 64
}

task("buildAll") {
    dependsOn("buildSprite", "resizePng")
    val outputFile = File("plantuml/FirebaseAll.puml")
    outputFile.createNewFile()
    val output = PrintWriter(outputFile.outputStream())
    generatePuml.files.forEach() {
        output.println(it.readText())
    }
    output.flush()
    output.close()
}
task("setIconsToReadme") {
    dependsOn("buildAll")
    val pattern = Pattern.compile("^(<!-- icons-begin -->\\s(.*)<!-- icons-end -->)$", Pattern.MULTILINE or Pattern.DOTALL)
    val table = StringBuilder().apply {
        append("<!-- icons-begin -->\n")
        append("|  Product  |  Icon  | Rectangle | Participant |\n")
        append("| --------- | ------ | --------- | ----------- |\n")
    }
    fileTree("plantuml").matching {
        include("**/*.png")
    }.visit {
        if (!file.isFile) {
            return@visit
        }
        table.append("| ${relativePath.parent.pathString} | ![](./plantuml/${relativePath.pathString}) | ${file.nameWithoutExtension}(alias, \"\", \"\") | ${file.nameWithoutExtension}Participant(alias, \"\", \"\") |\n")
    }
    table.append("<!-- icons-end -->")
    val readme = File("README.md")
    readme.writeText(readme.readText().replace(pattern.toRegex(), table.toString()))
}


task("buildAssets") {
    dependsOn("buildAll")
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

tasks["build"].dependsOn("buildAll", "setIconsToReadme", "buildAssets")

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

open class ResizePngTask : DefaultTask() {
    @InputFiles
    open lateinit var inputFiles: FileTree

    open var outputWidth: Int = 128
    open var outputHeight: Int = 128

    open var outputDirectory: File? = null

    @TaskAction
    fun exec() {
        inputFiles.visit {
            if (!file.isFile) {
                return@visit
            }
            val outputFile = File(
                    relativePath.getFile(outputDirectory?.absoluteFile ?: file.parentFile).parent,
                    "${file.nameWithoutExtension}.png")
            outputFile.parentFile.mkdirs()
            outputFile.createNewFile()
            convert(file, outputFile)
        }
    }

    private fun convert(`in`: File, out: File) {
        val input = ImageIO.read(`in`)

        var width = input.width
        var height = input.height
        var targetw = outputWidth
        val targeth = outputHeight

        do {
            if (width > targetw) {
                width /= 2
                if (width < targetw) width = targetw
            }

            if (height > targeth) {
                height /= 2
                if (height < targeth) height = targeth
            }
        } while (width != targetw || height != targeth)

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = bufferedImage.createGraphics()
        graphics2D.composite = AlphaComposite.Src
        graphics2D.background = Color(0, 0, 0, 0)
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics2D.drawImage(input, 0, 0, width, height, null)
        graphics2D.dispose()


        ImageIO.write(bufferedImage, "png", out)
    }
}

open class BuildSpriteTask : DefaultTask() {
    @InputFiles
    open lateinit var inputFiles: FileTree

    open var outputDirectory: File? = null

    val colorPatterns = mapOf(
            "Firestore" to "FIREBASE_YELLOW",
            "MLKit" to "FIREBASE_YELLOW",
            "Functions" to "FIREBASE_YELLOW",
            "Authentication" to "FIREBASE_YELLOW",
            "Hosting" to "FIREBASE_YELLOW",
            "Storage" to "FIREBASE_YELLOW",
            "RealtimeDatabase" to "FIREBASE_YELLOW",
            "Extensions" to "FIREBASE_YELLOW",

            "Crashlytics" to "FIREBASE_AMBER",
            "AppDistribution" to "FIREBASE_AMBER",
            "PerformanceMonitoring" to "FIREBASE_AMBER",
            "TestLab" to "FIREBASE_AMBER"
    )

    @TaskAction
    fun exec() {
        inputFiles.visit {
            if (!file.isFile) {
                return@visit
            }
            val outputFile = File(
                    relativePath.getFile(outputDirectory?.absoluteFile ?: file.parentFile).parent,
                    "${file.nameWithoutExtension}.puml")
            outputFile.createNewFile()
            val input = file.absoluteFile.inputStream()
            val output = outputFile.outputStream()
            try {
                convert(file.nameWithoutExtension, relativePath, input, output)
            } finally {
                input.close()
                output.close()
            }
        }
    }

    private fun convert(name: String, relativePath: RelativePath, `in`: InputStream, out: OutputStream) {
        val input = ImageIO.read(`in`)
        val output = PrintWriter(out)
        val sprite = SpriteUtils.encodeCompressed(input, name, SpriteGrayLevel.GRAY_16)
        output.println(sprite.replace("\\r\\n|\\r", "\n"))
        output.println()
        output.println("FirebaseEntityColoring($name)")
        val color = colorPatterns[name]?: "FIREBASE_CORAL"
        output.println("!define ${name}(alias, label, part_desc) FirebaseEntity(alias, label, part_desc, $color, $name, $name)")
        output.println("!define ${name}Participant(alias, label, part_desc) FirebaseParticipant(alias, label, part_desc, $color, $name, $name)")
        output.flush()
    }
}