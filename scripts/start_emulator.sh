#!/bin/bash
if [[ "$TRAVIS_PULL_REQUEST" == "false" ]] ; then
    echo 'count=0' > /home/travis/.android/repositories.cfg # Avoid harmless sdkmanager warning
    echo y | sdkmanager "platform-tools" >/dev/null
    echo y | sdkmanager "tools" >/dev/null # A second time per Travis docs, gets latest versions
    echo y | sdkmanager "build-tools;28.0.3" >/dev/null # Implicit gradle dependency - gradle drives changes
    echo y | sdkmanager "platforms;android-$API" >/dev/null # We need the API of the emulator we will run
    echo y | sdkmanager "platforms;android-28" >/dev/null # We need the API of the current compileSdkVersion from gradle.properties
    echo y | sdkmanager --channel=4 "emulator" # Experiment with canary, specifying 28.0.3 (prior version) did not work
    echo y | sdkmanager "extras;android;m2repository" >/dev/null
    echo y | sdkmanager "system-images;android-$API;$EMU_FLAVOR;$ABI" #>/dev/null # install our emulator
    echo no | avdmanager create avd --force -n test -k "system-images;android-$API;$EMU_FLAVOR;$ABI" -c 10M
    emulator -verbose -avd test -no-accel -no-snapshot -no-window $AUDIO -camera-back none -camera-front none -selinux permissive -qemu -m 2048 &
    android-wait-for-emulator
    adb shell settings put global window_animation_scale 0 &
    adb shell settings put global transition_animation_scale 0 &
    adb shell settings put global animator_duration_scale 0 &
    adb shell input keyevent 82 &

    else echo "Skipping emulator install because the current build is a pull request."
fi
