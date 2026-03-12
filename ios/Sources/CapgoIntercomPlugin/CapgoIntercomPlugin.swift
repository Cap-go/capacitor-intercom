import Foundation
import Capacitor
import Intercom

@objc(CapgoIntercomPlugin)
public class CapgoIntercomPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "CapgoIntercomPlugin"
    public let jsName = "CapgoIntercom"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "loadWithKeys", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "registerIdentifiedUser", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "registerUnidentifiedUser", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "updateUser", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "logout", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "logEvent", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displayMessenger", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displayMessageComposer", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displayHelpCenter", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "hideMessenger", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displayLauncher", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "hideLauncher", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displayInAppMessages", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "hideInAppMessages", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displayCarousel", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displayArticle", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "displaySurvey", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "setUserHash", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "setUserJwt", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "setBottomPadding", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "sendPushTokenToIntercom", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "receivePush", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "getUnreadConversationCount", returnType: CAPPluginReturnPromise)
    ]

    private var showObserver: NSObjectProtocol?
    private var hideObserver: NSObjectProtocol?
    private var pushObserver: NSObjectProtocol?
    private var unreadObserver: NSObjectProtocol?

    override public func load() {
        let apiKey = getConfig().getString("iosApiKey") ?? ""
        let appId = getConfig().getString("iosAppId") ?? ""

        if !apiKey.isEmpty && !appId.isEmpty {
            Intercom.setApiKey(apiKey, forAppId: appId)
            #if DEBUG
            Intercom.enableLogging()
            #endif
        }

        pushObserver = NotificationCenter.default.addObserver(
            forName: .capacitorDidRegisterForRemoteNotifications,
            object: nil,
            queue: .main
        ) { notification in
            if let deviceToken = notification.object as? Data {
                Intercom.setDeviceToken(deviceToken, completion: nil)
            }
        }

        showObserver = NotificationCenter.default.addObserver(
            forName: NSNotification.Name("IntercomWindowDidShowNotification"),
            object: nil,
            queue: .main
        ) { [weak self] _ in
            self?.notifyListeners("windowDidShow", data: [:])
        }

        hideObserver = NotificationCenter.default.addObserver(
            forName: NSNotification.Name("IntercomWindowDidHideNotification"),
            object: nil,
            queue: .main
        ) { [weak self] _ in
            self?.notifyListeners("windowDidHide", data: [:])
        }

        unreadObserver = NotificationCenter.default.addObserver(
            forName: NSNotification.Name.IntercomUnreadConversationCountDidChange,
            object: nil,
            queue: .main
        ) { [weak self] _ in
            let count = Intercom.unreadConversationCount()
            self?.notifyListeners("unreadCountDidChange", data: ["count": count])
        }
    }

    deinit {
        if let obs = showObserver {
            NotificationCenter.default.removeObserver(obs)
        }
        if let obs = hideObserver {
            NotificationCenter.default.removeObserver(obs)
        }
        if let obs = pushObserver {
            NotificationCenter.default.removeObserver(obs)
        }
        if let obs = unreadObserver {
            NotificationCenter.default.removeObserver(obs)
        }
    }

    @objc func loadWithKeys(_ call: CAPPluginCall) {
        let appId = call.getString("appId") ?? ""
        let apiKey = call.getString("apiKeyIOS") ?? ""

        guard !appId.isEmpty && !apiKey.isEmpty else {
            call.reject("appId and apiKeyIOS are required")
            return
        }

        Intercom.setApiKey(apiKey, forAppId: appId)
        #if DEBUG
        Intercom.enableLogging()
        #endif
        call.resolve()
    }

    @objc func registerIdentifiedUser(_ call: CAPPluginCall) {
        let userId = call.getString("userId")
        let email = call.getString("email")

        let attributes = ICMUserAttributes()
        if let userId = userId {
            attributes.userId = userId
        }
        if let email = email {
            attributes.email = email
        }

        Intercom.loginUser(with: attributes) { result in
            switch result {
            case .success:
                call.resolve()
            case .failure(let error):
                call.reject("Failed to register user: \(error.localizedDescription)")
            }
        }
    }

    @objc func registerUnidentifiedUser(_ call: CAPPluginCall) {
        Intercom.loginUnidentifiedUser { result in
            switch result {
            case .success:
                call.resolve()
            case .failure(let error):
                call.reject("Failed to register unidentified user: \(error.localizedDescription)")
            }
        }
    }

    @objc func updateUser(_ call: CAPPluginCall) {
        let attributes = ICMUserAttributes()

        if let userId = call.getString("userId") {
            attributes.userId = userId
        }
        if let email = call.getString("email") {
            attributes.email = email
        }
        if let name = call.getString("name") {
            attributes.name = name
        }
        if let phone = call.getString("phone") {
            attributes.phone = phone
        }
        if let languageOverride = call.getString("languageOverride") {
            attributes.languageOverride = languageOverride
        }
        if let customAttributes = call.getObject("customAttributes") {
            attributes.customAttributes = customAttributes as [String: Any]
        }

        if let companiesArray = call.getArray("companies") as? [[String: Any]] {
            var companies: [ICMCompany] = []
            for companyData in companiesArray {
                let company = ICMCompany()
                if let companyId = companyData["companyId"] as? String {
                    company.companyId = companyId
                }
                if let name = companyData["name"] as? String {
                    company.name = name
                }
                if let plan = companyData["plan"] as? String {
                    company.plan = plan
                }
                if let monthlySpend = companyData["monthlySpend"] as? NSNumber {
                    company.monthlySpend = monthlySpend
                }
                if let createdAt = companyData["createdAt"] as? Double {
                    company.createdAt = Date(timeIntervalSince1970: createdAt)
                }
                if let customAttrs = companyData["customAttributes"] as? [String: Any] {
                    company.customAttributes = customAttrs
                }
                companies.append(company)
            }
            attributes.companies = companies
        }

        Intercom.updateUser(with: attributes)
        call.resolve()
    }

    @objc func logout(_ call: CAPPluginCall) {
        Intercom.logout()
        call.resolve()
    }

    @objc func logEvent(_ call: CAPPluginCall) {
        guard let name = call.getString("name") else {
            call.reject("Event name is required")
            return
        }

        if let data = call.getObject("data") {
            Intercom.logEvent(withName: name, metaData: data as [AnyHashable: Any])
        } else {
            Intercom.logEvent(withName: name)
        }
        call.resolve()
    }

    @objc func displayMessenger(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            Intercom.present()
            call.resolve()
        }
    }

    @objc func displayMessageComposer(_ call: CAPPluginCall) {
        guard let message = call.getString("message") else {
            call.reject("message is required")
            return
        }

        DispatchQueue.main.async {
            Intercom.presentMessageComposer(message)
            call.resolve()
        }
    }

    @objc func displayHelpCenter(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            Intercom.present(.helpCenter)
            call.resolve()
        }
    }

    @objc func hideMessenger(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            Intercom.hide()
            call.resolve()
        }
    }

    @objc func displayLauncher(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            Intercom.setLauncherVisible(true)
            call.resolve()
        }
    }

    @objc func hideLauncher(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            Intercom.setLauncherVisible(false)
            call.resolve()
        }
    }

    @objc func displayInAppMessages(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            Intercom.setInAppMessagesVisible(true)
            call.resolve()
        }
    }

    @objc func hideInAppMessages(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            Intercom.setInAppMessagesVisible(false)
            call.resolve()
        }
    }

    @objc func displayCarousel(_ call: CAPPluginCall) {
        guard let carouselId = call.getString("carouselId") else {
            call.reject("carouselId is required")
            return
        }

        DispatchQueue.main.async {
            Intercom.presentContent(.carousel(id: carouselId))
            call.resolve()
        }
    }

    @objc func displayArticle(_ call: CAPPluginCall) {
        guard let articleId = call.getString("articleId") else {
            call.reject("articleId is required")
            return
        }

        DispatchQueue.main.async {
            Intercom.presentContent(.article(id: articleId))
            call.resolve()
        }
    }

    @objc func displaySurvey(_ call: CAPPluginCall) {
        guard let surveyId = call.getString("surveyId") else {
            call.reject("surveyId is required")
            return
        }

        DispatchQueue.main.async {
            Intercom.presentContent(.survey(id: surveyId))
            call.resolve()
        }
    }

    @objc func setUserHash(_ call: CAPPluginCall) {
        guard let hmac = call.getString("hmac") else {
            call.reject("hmac is required")
            return
        }

        Intercom.setUserHash(hmac)
        call.resolve()
    }

    @objc func setUserJwt(_ call: CAPPluginCall) {
        guard let jwt = call.getString("jwt") else {
            call.reject("jwt is required")
            return
        }

        Intercom.setUserHash(jwt)
        call.resolve()
    }

    @objc func setBottomPadding(_ call: CAPPluginCall) {
        guard let value = call.getDouble("value") else {
            call.reject("value is required")
            return
        }

        DispatchQueue.main.async {
            Intercom.setBottomPadding(CGFloat(value))
            call.resolve()
        }
    }

    @objc func sendPushTokenToIntercom(_ call: CAPPluginCall) {
        guard let token = call.getString("value") else {
            call.reject("value is required")
            return
        }

        if let tokenData = token.data(using: .utf8) {
            DispatchQueue.main.async {
                Intercom.setDeviceToken(tokenData, completion: nil)
            }
        }
        call.resolve()
    }

    @objc func receivePush(_ call: CAPPluginCall) {
        let userInfo = call.options as? [AnyHashable: Any] ?? [:]
        DispatchQueue.main.async {
            if Intercom.isIntercomPushNotification(userInfo) {
                Intercom.handlePushNotification(userInfo)
            }
        }
        call.resolve()
    }

    @objc func getUnreadConversationCount(_ call: CAPPluginCall) {
        let count = Intercom.unreadConversationCount()
        call.resolve(["count": count])
    }
}
