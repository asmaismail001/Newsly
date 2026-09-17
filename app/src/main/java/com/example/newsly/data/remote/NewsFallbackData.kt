package com.example.newsly.data.remote

import com.example.newsly.data.model.NewsArticle

object NewsFallbackData {

    fun getFallbackArticles(category: String = "All"): List<NewsArticle> {
        val allArticles = listOf(
            NewsArticle(
                id = "art-breaking-1",
                title = "Global AI Summit Unveils Next-Gen Autonomous Systems and Breakthrough Quantum Computing",
                description = "World technology leaders gather in Geneva to establish ethical frameworks and showcase quantum machine learning architectures capable of complex problem solving.",
                content = "World technology leaders gathered today at the prestigious International AI and Quantum Summit in Geneva. Breakthrough demonstrations highlighted real-time generative translation, autonomous climate modeling, and quantum neural networks operating at unprecedented processing speeds. Keynote speakers emphasized the necessity for open standards, cross-border safety pacts, and accessible computing power for developing economies worldwide.",
                url = "https://www.bbc.com/news/technology",
                imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=1200&q=80",
                videoUrl = "https://raw.githubusercontent.com/mediaelement/mediaelement-files/master/big_buck_bunny.mp4",
                videoDuration = "03:45",
                publishedAt = "2026-09-06T13:45:00Z",
                sourceName = "BBC News",
                author = "Eleanor Vance",
                category = "Technology",
                isBreaking = true
            ),
            NewsArticle(
                id = "art-tech-2",
                title = "Next-Generation Neural Chips Promise 10x Battery Life in Mobile Devices",
                description = "Semiconductor pioneers unveil ultra-efficient 2nm architecture optimized specifically for on-device machine intelligence without cloud latency.",
                content = "Semiconductor engineers have announced the commercial availability of 2nm neural compute units designed specifically for mobile edge computing. The new architecture promises to reduce power consumption by up to 70% while accelerating localized model inference tenfold.",
                url = "https://techcrunch.com",
                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1200&q=80",
                videoUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
                videoDuration = "04:12",
                publishedAt = "2026-09-06T12:30:00Z",
                sourceName = "TechCrunch",
                author = "Marcus Reed",
                category = "Technology",
                isBreaking = true
            ),
            NewsArticle(
                id = "art-world-1",
                title = "Historic Green Energy Pact Signed by 45 Nations to Accelerate Clean Infrastructure",
                description = "International delegation ratifies landmark transition roadmap targeting 100% renewable municipal grids within the decade.",
                content = "Delegates from 45 countries officially signed the International Clean Energy Transition Accord in Vienna. The initiative pledges $300 billion in cross-border funding for grid modernization, solar thermal arrays, and green hydrogen storage facilities.",
                url = "https://www.reuters.com",
                imageUrl = "https://images.unsplash.com/photo-1466611653911-95081537e5b7?auto=format&fit=crop&w=1200&q=80",
                videoUrl = "https://vjs.zencdn.net/v/oceans.mp4",
                videoDuration = "02:15",
                publishedAt = "2026-09-06T11:15:00Z",
                sourceName = "Reuters",
                author = "Sophia Martinez",
                category = "World",
                isBreaking = true
            ),
            NewsArticle(
                id = "art-business-1",
                title = "Global Markets Rally as Inflation Drops to Five-Year Low Amid Manufacturing Boom",
                description = "Stock indices across Tokyo, London, and New York surge following encouraging quarterly financial indicators and robust trade volumes.",
                content = "Global equity markets experienced widespread gains on Monday following the release of comprehensive international trade data. Benchmark indices climbed by an average of 2.4%, driven by strong performances in manufacturing, clean tech, and consumer electronics.",
                url = "https://www.bloomberg.com",
                imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=1200&q=80",
                videoUrl = null,
                publishedAt = "2026-09-06T10:00:00Z",
                sourceName = "Bloomberg",
                author = "David Sterling",
                category = "Business"
            ),
            NewsArticle(
                id = "art-science-1",
                title = "Deep Space Telescope Discovers Atmospheric Water Vapor on Nearby Exoplanet",
                description = "Astrophysicists confirm spectroscopic signatures of water clouds in habitable zone star system 35 light years away.",
                content = "Astronomers analyzing data from the James Webb Space Telescope have detected definitive atmospheric water vapor signatures on exoplanet K2-18b. The discovery represents one of the most promising candidates for extraterrestrial atmospheric study to date.",
                url = "https://www.nature.com",
                imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=1200&q=80",
                videoUrl = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8",
                videoDuration = "05:00",
                publishedAt = "2026-09-06T09:20:00Z",
                sourceName = "Nature Science",
                author = "Dr. Aris Thorne",
                category = "Science"
            ),
            NewsArticle(
                id = "art-health-1",
                title = "Groundbreaking Personalized mRNA Therapy Enters Phase 3 Clinical Trials",
                description = "Medical researchers celebrate major milestone in targeted immunotherapy with high efficacy and minimal side effects.",
                content = "A clinical trial involving over 5,000 patients has demonstrated high efficacy for tailored mRNA immunotherapy treatments. The therapy trains the body's immune system to identify and neutralize mutated cellular proteins.",
                url = "https://www.medicalnewstoday.com",
                imageUrl = "https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?auto=format&fit=crop&w=1200&q=80",
                videoUrl = null,
                publishedAt = "2026-09-06T08:10:00Z",
                sourceName = "Healthline",
                author = "Dr. Claire Jensen",
                category = "Health"
            ),
            NewsArticle(
                id = "art-sports-1",
                title = "Champions League Thriller: Dramatic 94th-Minute Winner Sends Stadium into Frenzy",
                description = "Extraordinary semi-final second leg delivers non-stop excitement and historic turnaround in front of 80,000 fans.",
                content = "In one of the most dramatic Champions League encounters in recent history, an extraordinary stoppage-time volley secured a place in the grand final. The stadium erupted as the referee blew the final whistle on a 3-2 victory.",
                url = "https://www.skysports.com",
                imageUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80",
                videoUrl = "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                videoDuration = "01:50",
                publishedAt = "2026-09-06T07:45:00Z",
                sourceName = "Sky Sports",
                author = "Liam Walker",
                category = "Sports"
            ),
            NewsArticle(
                id = "art-entertainment-1",
                title = "Venice Film Festival Awards Golden Lion to Dazzling Cinematic Masterpiece",
                description = "Visionary indie director earns standing ovation and highest honors at the 83rd Venice International Film Festival.",
                content = "The 83rd Venice Film Festival concluded with thunderous applause as the prestigious Golden Lion was awarded to the breathtaking indie feature exploring human connection in the digital age.",
                url = "https://variety.com",
                imageUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=1200&q=80",
                videoUrl = null,
                publishedAt = "2026-09-06T06:30:00Z",
                sourceName = "Variety",
                author = "Chloe Dupont",
                category = "Entertainment"
            ),
            NewsArticle(
                id = "art-environment-1",
                title = "Reforestation Drone Fleets Plant 10 Million Trees Across Degraded Watersheds",
                description = "Innovative autonomous seed-dropping drones accelerate ecological restoration with an 85% germination success rate.",
                content = "A pioneering conservation initiative utilizing autonomous drone swarms has successfully planted 10 million native trees across critical watersheds. The smart pods carry nutrients and mycorrhizal fungi to maximize survival.",
                url = "https://www.nationalgeographic.com",
                imageUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=1200&q=80",
                videoUrl = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8",
                videoDuration = "03:10",
                publishedAt = "2026-09-06T05:15:00Z",
                sourceName = "Nat Geo",
                author = "Noah Patel",
                category = "Environment"
            ),
            NewsArticle(
                id = "art-education-1",
                title = "Interactive AI Tutors Adopted by High Schools to Personalize Math & Science Learning",
                description = "Early studies show 40% improvement in student retention and confidence when using real-time conversational guides.",
                content = "Educational institutions across North America and Europe are integrating conversational AI learning assistants to augment classroom teaching. The tools adapt difficulty in real time to each student's pacing and learning style.",
                url = "https://www.edutopia.org",
                imageUrl = "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?auto=format&fit=crop&w=1200&q=80",
                videoUrl = null,
                publishedAt = "2026-09-06T04:00:00Z",
                sourceName = "Edutopia",
                author = "Hannah Kim",
                category = "Education"
            ),
            NewsArticle(
                id = "art-lifestyle-1",
                title = "The Architecture of Rest: How Modern Urban Homes Are Designing for Deep Silence",
                description = "Acoustic engineering and biophilic interior spaces are transforming hectic city apartments into serene wellness sanctuaries.",
                content = "Modern urban architecture is experiencing a fundamental shift toward acoustic wellness. Architects are integrating organic cork baffling, sound-absorbing greenery, and natural airflow to create tranquil living spaces in bustling city centers.",
                url = "https://www.architecturaldigest.com",
                imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?auto=format&fit=crop&w=1200&q=80",
                videoUrl = null,
                publishedAt = "2026-09-06T03:20:00Z",
                sourceName = "Design Digest",
                author = "Julian Rossi",
                category = "Lifestyle"
            ),
            NewsArticle(
                id = "art-politics-1",
                title = "Parliament Passes Comprehensive Consumer Digital Rights and Privacy Legislation",
                description = "New regulatory framework sets global benchmark for algorithmic transparency and personal data sovereignty.",
                content = "Lawmakers voted overwhelmingly to enact the Consumer Digital Rights Act today. The legislation mandates transparent algorithms, strict consent mechanisms, and clear right-to-repair guidelines for consumer technology.",
                url = "https://www.politico.com",
                imageUrl = "https://images.unsplash.com/photo-1541872703-74c5e44368f9?auto=format&fit=crop&w=1200&q=80",
                videoUrl = null,
                publishedAt = "2026-09-06T02:00:00Z",
                sourceName = "Politico",
                author = "Samuel Greene",
                category = "Politics"
            ),
            NewsArticle(
                id = "art-local-1",
                title = "Metro Rapid Transit Line Extension Opens Ahead of Schedule to Acclaim",
                description = "New eco-friendly high-capacity subway line connects suburban communities to the city center in under 18 minutes.",
                content = "City officials and community members celebrated the ribbon-cutting for the newly completed Metro Rapid Transit line extension today. The electric line is projected to eliminate over 25,000 daily car trips.",
                url = "https://www.citynews.com",
                imageUrl = "https://images.unsplash.com/photo-1517649763962-0c623266ddc0?auto=format&fit=crop&w=1200&q=80",
                videoUrl = null,
                publishedAt = "2026-09-06T01:30:00Z",
                sourceName = "City Gazette",
                author = "Maya Lin",
                category = "Local News"
            )
        )

        return when {
            category.equals("All", ignoreCase = true) -> allArticles
            category.equals("Breaking", ignoreCase = true) || category.contains("Breaking", ignoreCase = true) -> {
                allArticles.filter { it.isBreaking }.ifEmpty { allArticles.take(3) }
            }
            category.equals("Videos", ignoreCase = true) -> {
                allArticles.filter { !it.videoUrl.isNullOrBlank() }
            }
            else -> {
                val filtered = allArticles.filter { it.category.equals(category, ignoreCase = true) }
                filtered.ifEmpty { allArticles }
            }
        }
    }
}
