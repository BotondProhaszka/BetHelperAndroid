package hu.proha.bethelper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.proha.bethelper.R
import hu.proha.bethelper.data.Match
import hu.proha.bethelper.databinding.FragmentMatchesBinding

class MatchesFragment : Fragment() {

    private lateinit var matchesAdapter: MatchesAdapter
    private lateinit var binding: FragmentMatchesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMatchesBinding.inflate(inflater, container, false)

        matchesAdapter = MatchesAdapter()
        binding.matchesRecyclerView.adapter = matchesAdapter
        binding.matchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val matches = arguments?.getParcelableArrayList<Match>("matches")
        matches?.let {
            matchesAdapter.submitList(matches)
        }

        return binding.root
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

    class MatchesAdapter(private val matches: List<Match> = arrayListOf()) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val homeTeam: TextView = itemView.findViewById(R.id.home_team)
            val score: TextView = itemView.findViewById(R.id.score)
            val awayTeam: TextView = itemView.findViewById(R.id.away_team)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.match_card, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val match = matches[position]
            holder.homeTeam.text = match.homeTeam.name
            holder.score.text = match.score.homeTeam.toString() + " - " + match.score.awayTeam.toString()
            holder.awayTeam.text = match.awayTeam.name
        }

        override fun getItemCount() = matches.size
        fun submitList(matches: ArrayList<Match>) {

        }
    }

}
