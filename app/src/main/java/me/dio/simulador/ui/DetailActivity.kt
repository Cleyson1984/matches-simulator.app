package me.dio.simulador.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import me.dio.simulador.databinding.ActivityDetailBinding
import me.dio.simulador.domain.Match

class DetailActivity : AppCompatActivity() {

    object Extra {
        const val MATCH = "EXTRA_MATCH"

    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        loadMatchFromExtra()
    }

    private fun loadMatchFromExtra() {
        intent?.extras?.getParcelable<Match>(Extra.MATCH)?.let {
            Glide.with(this).load(it.place.imagen).into(binding.ivPlace)
            supportActionBar?.title = it.place.name

            binding.tvDescription.text = it.description

            Glide.with(this).load(it.homeTeam.image).into(binding.ivHomeTeam)
            binding.tvHomeTeamName.text = it.homeTeam.name
            binding.rbHomeTeamStars.rating = it.homeTeam.stars.toFloat()
            if (it.homeTeam.score != null){
                binding.tvHomeTeamScore.text = it.homeTeam.score.toString()
            }

            Glide.with(this).load(it.awayTeam.image).into(binding.ivAwayTeam)
            binding.tvAwayTeamName.text = it.awayTeam.name
            binding.rbAwayTeamStars.rating = it.awayTeam.stars.toFloat()
            if (it.awayTeam.score != null){
                binding.tvAwayTeamScore.text = it.awayTeam.score.toString()
            }
        }
    }
}