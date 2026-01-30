// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapgoCapacitorIntercom",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "CapgoCapacitorIntercom",
            targets: ["CapgoIntercomPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0"),
        .package(url: "https://github.com/intercom/intercom-ios-sp.git", from: "19.0.0")
    ],
    targets: [
        .target(
            name: "CapgoIntercomPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm"),
                .product(name: "Intercom", package: "intercom-ios-sp")
            ],
            path: "ios/Sources/CapgoIntercomPlugin"),
        .testTarget(
            name: "CapgoIntercomPluginTests",
            dependencies: ["CapgoIntercomPlugin"],
            path: "ios/Tests/CapgoIntercomPluginTests")
    ]
)
