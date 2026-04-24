import Foundation
import SwiftUI

struct RestaurantPreview: Identifiable {
    let id: String
    let name: String
    let cuisine: String
    let emoji: String
    let menu: [String]
}

enum Sample {
    static let primary = Color(red: 1.0, green: 0.42, blue: 0.21)
    static let accent = Color(red: 0.30, green: 0.69, blue: 0.31)

    static let restaurants: [RestaurantPreview] = [
        RestaurantPreview(
            id: "1",
            name: "Pizza Place",
            cuisine: "Italian, Pizza",
            emoji: "🍕",
            menu: ["Margherita Pizza", "Pepperoni Pizza", "French Fries"]
        ),
        RestaurantPreview(
            id: "2",
            name: "Burger House",
            cuisine: "Burgers, Fast Food",
            emoji: "🍔",
            menu: ["Chicken Burger", "Beef Burger", "Loaded Fries"]
        ),
        RestaurantPreview(
            id: "3",
            name: "Sushi World",
            cuisine: "Sushi, Japanese",
            emoji: "🍣",
            menu: ["California Roll", "Salmon Nigiri", "Miso Soup"]
        )
    ]
}
