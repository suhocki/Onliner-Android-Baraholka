#!/bin/bash
if [[ "$TRAVIS_PULL_REQUEST" == "false" ]] ; then
    sdkmanager "platforms;android-${EMU_API}" | tr '\r' '\n' | uniq
    sdkmanager emulator | tr '\r' '\n' | uniq
    sdkmanager "system-images;android-${EMU_API};${EMU_FLAVOR};${EMU_ABI}" | tr '\r' '\n' | uniq
    # Allow use of KVM
    sudo adduser $USER libvirt
    sudo adduser $USER kvm
    # Create and start emulator as early as possible
    adb start-server
    avdmanager create avd --force --name test --package "system-images;android-${EMU_API};${EMU_FLAVOR};${EMU_ABI}" --abi ${EMU_ABI} --device 'Nexus 4' --sdcard 128M
    sudo -E sudo -u $USER -E bash -c "$ANDROID_HOME/emulator/emulator-headless -avd test -skin 768x1280 -no-audio -no-window -no-boot-anim -no-snapshot -camera-back none -camera-front none -qemu -m 2048 &"
    sdkmanager 'build-tools;29.0.2' | tr '\r' '\n' | uniq
    sdkmanager 'platforms;android-28' | tr '\r' '\n' | uniq
    sdkmanager 'extras;android;m2repository' | tr '\r' '\n' | uniq
    sdkmanager 'extras;google;m2repository' | tr '\r' '\n' | uniq
    sdkmanager 'extras;google;google_play_services' | tr '\r' '\n' | uniq
    # Download the emulator support stuff
    mkdir -p $HOME/.cache/ci-support
    curl -L https://github.com/connectbot/ci-support/archive/master.zip -z $HOME/.cache/ci-support/master.zip -o $HOME/.cache/ci-support/master.zip
    unzip -oq $HOME/.cache/ci-support/master.zip -d $HOME
    mkdir -p $HOME/bin
    curl -L https://raw.githubusercontent.com/travis-ci/travis-cookbooks/master/community-cookbooks/android-sdk/files/default/android-wait-for-emulator -o $HOME/bin/android-wait-for-emulator
    chmod +x $HOME/bin/android-wait-for-emulator
    # Try to download Gradle deps while Android is booting
    ./gradlew --parallel -Dorg.gradle.parallel.intra=true resolveDependencies

    else echo "Skipping emulator install because the current build is a pull request."
fi
