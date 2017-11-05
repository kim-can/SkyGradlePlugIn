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
        System.out.println("taskNames is " + taskNames);
        String module = project.path.replace(":", "")
        System.out.println("current module is " + module);

        int moduleLength = module.split("_").length
        if (moduleLength < 2) {
            throw new RuntimeException("you module name is not module_*** and cpt_***  not like this")
        }

        String resourcePrefixS = module.split("_")[moduleLength - 1] + "_";
        System.out.println("resourcePrefixS is " + module);

        if (!project.hasProperty("isRunAlone")) {
            throw new RuntimeException("you should set isRunAlone in " + module + "'s gradle.properties")
        }

        boolean isRunAlone = project.properties.get("isRunAlone")

        if (project.hasProperty("isJenkinsRunAlone")) { //如果存在自动化打包
            isRunAlone = project.properties.get("isJenkinsRunAlone")
        }

        if (isRunAlone) {
            project.apply plugin: 'com.android.application'
            System.out.println("apply plugin is " + 'com.android.application');

            project.android.sourceSets {
                main {
                    manifest.srcFile 'src/main/module/AndroidManifest.xml'
                }
            }
            project.android.resourcePrefix resourcePrefixS
        } else {
            project.apply plugin: 'com.android.library'
            System.out.println("apply plugin is " + 'com.android.library');

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
    }

}
