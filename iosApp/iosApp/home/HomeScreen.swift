import SwiftUI

struct HomeScreen: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    VStack(alignment: .leading, spacing: 8) {
                        Text("ServeHub")
                            .font(.largeTitle.bold())
                            .foregroundColor(Sample.primary)
                        Text("Your Restaurant. Your Way.")
                            .foregroundColor(.secondary)
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        Text("iOS placeholder")
                            .font(.headline)
                        Text("This iOS host has been cleaned up from the old movie sample and is ready for ServeHub-specific screens.")
                            .foregroundColor(.secondary)
                    }

                    ForEach(Sample.restaurants) { restaurant in
                        NavigationLink(destination: DetailScreen(restaurant: restaurant)) {
                            MovieGridItem(restaurant: restaurant)
                        }
                        .buttonStyle(.plain)
                    }
                }
                .padding(20)
            }
            .navigationTitle("ServeHub")
        }
    }
}

struct HomeScreen_Previews: PreviewProvider {
    static var previews: some View {
        HomeScreen()
    }
}
