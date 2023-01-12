package hu.proha.bethelper.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.proha.bethelper.R
import hu.proha.bethelper.data.Match
import hu.proha.bethelper.databinding.FragmentMatchesBinding
import hu.proha.bethelper.services.FootballDataRepository
import hu.proha.bethelper.services.MatchStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchesFragment : Fragment() {

    private lateinit var matchesAdapter: MatchesAdapter
    private lateinit var binding: FragmentMatchesBinding
    private lateinit var matchStorage: MatchStorage

    private var matches : ArrayList<Match> = arrayListOf()
    private var footballDataRepository = FootballDataRepository()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMatchesBinding.inflate(inflater, container, false)

        matchStorage = MatchStorage(this.requireContext())
        matchesAdapter = MatchesAdapter()
        binding.matchesRecyclerView.adapter = matchesAdapter
        binding.matchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val matches = arguments?.getParcelableArrayList<Match>("matches")
        matches?.let {
            matchesAdapter.submitList(matches)
        }

        binding.fabRefresh.setOnClickListener {
            fab_load()
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fab_load(){

        CoroutineScope(Dispatchers.IO).launch {
            //matches = ArrayList(footballDataRepository.getMatchesByCountryWithDate("DE", "2022-11-10", "2022-11-15"))
            matches = ArrayList(footballDataRepository.getMatches())

            withContext(Dispatchers.Main) {
                Log.d("ASD", "Num of matches: ${matches.size}")
                matchesAdapter.submitList(matches)
                matchesAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun loadSavedMatches() {
        matches = matchStorage.loadMatches()
        // Use the matches to update the UI
    }

    private fun saveMatches() {
        matchStorage.saveMatches(matches)
    }


    companion object {
        fun newInstance(matches: ArrayList<Match>): MatchesFragment {
            val fragment = MatchesFragment()
            val args = Bundle()
            args.putParcelableArrayList("matches", matches)
            fragment.arguments = args
            return fragment
        }
    }

    class MatchesAdapter(private var matches: List<Match> = arrayListOf()) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val homeTeam: TextView = itemView.findViewById(R.id.home_team)
            val score: TextView = itemView.findViewById(R.id.score)
            val awayTeam: TextView = itemView.findViewById(R.id.away_team)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.match_card, parent, false)
            return ViewHolder(itemView)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val match = matches[position]
            holder.homeTeam.text = match.homeTeam.name
            holder.score.text = "${match.score.homeTeam} - ${match.score.awayTeam}"
            holder.awayTeam.text = match.awayTeam.name
        }

        override fun getItemCount() = matches.size
        fun submitList(matches: List<Match>) {
            this.matches = matches
        }
    }

}
