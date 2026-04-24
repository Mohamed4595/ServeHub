import SwiftUI

struct DetailScreen: View {
    let restaurant: RestaurantPreview

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                RoundedRectangle(cornerRadius: 24)
                    .fill(
                        LinearGradient(
                            colors: [.orange, .red.opacity(0.7)],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .frame(height: 220)
                    .overlay(
                        Text(restaurant.emoji)
                            .font(.system(size: 80))
                    )

                VStack(alignment: .leading, spacing: 12) {
                    Text(restaurant.name)
                        .font(.title.bold())
                    Text("\(restaurant.cuisine) • 30-40 min")
                        .foregroundColor(.secondary)

                    Text("Popular Menu")
                        .font(.headline)

                    ForEach(restaurant.menu, id: \.self) { item in
                        HStack {
                            Text(item)
                            Spacer()
                            Text("+")
                                .foregroundColor(.white)
                                .frame(width: 28, height: 28)
                                .background(Sample.accent)
                                .clipShape(RoundedRectangle(cornerRadius: 8))
                        }
                        .padding()
                        .background(Color.white)
                        .clipShape(RoundedRectangle(cornerRadius: 16))
                        .shadow(color: .black.opacity(0.05), radius: 8, x: 0, y: 4)
                    }
                }
            }
            .padding(20)
        }
        .background(Color(.systemGroupedBackground))
    }
}

struct DetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        DetailScreen(restaurant: Sample.restaurants[0])
    }
}
