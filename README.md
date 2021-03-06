# Hipchat Notification plugin

This plugin can send notifications to HipChat.

## Getting started

* Edit the file `build.gradle`
* Edit the `GetPluginConfigurationExecutor.java` class to add any configuration fields that should be shown in the view.
* Edit the `plugin-settings.template.html` file which contains the view for the plugin settings page of your plugin.
* Edit the `PluginSettings.java` file which contains the model for your settings.
* Implement the `StageStatusRequestExecutor.java` class to get a basic notification plugin working.

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## Open problems

* I can't figure out how to prepopulate the configuration with default values
* There seems to be no indication whether the current stage is the last one in a pipeline. This prevents us from notifying only once per pipeline.

As a consequence: would using a task plugin be more appropriate?

## License

```plain
Copyright 2016 ThoughtWorks, Inc. and noris network AG

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
