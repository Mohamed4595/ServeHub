import Foundation

extension HomeScreen {
    @MainActor class HomeViewModel: ObservableObject {
        @Published private(set) var restaurants: [RestaurantPreview] = Sample.restaurants
    }
}
