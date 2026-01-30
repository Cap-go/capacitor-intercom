import XCTest
@testable import CapgoIntercomPlugin

class CapgoIntercomPluginTests: XCTestCase {
    func testPluginMethodsRegistered() {
        let plugin = CapgoIntercomPlugin()
        XCTAssertEqual(plugin.identifier, "CapgoIntercomPlugin")
        XCTAssertEqual(plugin.jsName, "CapgoIntercom")
        XCTAssertFalse(plugin.pluginMethods.isEmpty)
    }
}
