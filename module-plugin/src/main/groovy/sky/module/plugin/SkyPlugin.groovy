package sky.module.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author com.sky
 * @version 1.0 on 2017-11-05 上午11:58
 * @see SkyPlugin
 */
public class SkyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        String taskNames = project.gradle.startParameter.taskNames.toString()
        System.out.println("taskNames is " + taskNames)
        String module = project.path.replace(":", "")
        System.out.println("current module is " + module)

        int moduleLength = module.split("_").length
        if (moduleLength < 2) {
            throw new RuntimeException("you module name is not module_*** and cpt_***  not like this")
        }

        String resourcePrefixS = module.split("_")[moduleLength - 1] + "_"
        System.out.println("resourcePrefixS is " + resourcePrefixS)

        if (!project.hasProperty("isRunAlone")) {
            throw new RuntimeException("you should set isRunAlone in " + module + "'s gradle.properties")
        }

        if (!project.hasProperty("skyVersion")) {
            throw new RuntimeException("you should set skyVersion in " + module + "'s gradle.properties")
        }

        boolean isRunAlone = Boolean.valueOf(project.properties.get("isRunAlone"))
        String skyVersion = String.valueOf(project.properties.get("skyVersion"))
        System.out.println("isRunAlone 参数是:" + isRunAlone)

        if (isRunAlone) {
            project.apply plugin: 'com.android.application'
            System.out.println("apply plugin is " + 'com.android.application')

            project.android.sourceSets {
                main {
                    manifest.srcFile 'src/main/module/AndroidManifest.xml'
                }
            }
            project.android.resourcePrefix resourcePrefixS

        } else {
            project.apply plugin: 'com.android.library'
            System.out.println("apply plugin is " + 'com.android.library')

            project.android.sourceSets {
                main {
                    manifest.srcFile 'src/main/AndroidManifest.xml'
                    //集成开发模式下排除debug文件夹中的所有Java文件
                    java {
                        exclude 'debug/**'
                    }
                }
            }
            project.android.resourcePrefix resourcePrefixS
        }

        project.dependencies {
            // sky
            compile "com.jincanshen:sky:$skyVersion"
            // sky注解解析
            annotationProcessor "com.jincanshen:sky-compiler:$skyVersion"
        }
    }

}
