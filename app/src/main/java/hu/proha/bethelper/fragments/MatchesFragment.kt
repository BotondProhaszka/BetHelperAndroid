package hu.proha.bethelper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.proha.bethelper.R

class MatchesFragment : Fragment() {

    private lateinit var matchesAdapter: MatchesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_matches, container, false)

        matchesAdapter = MatchesAdapter()
        view.matchesRecyclerView.adapter = matchesAdapter
        view.matchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val matches = arguments?.getParcelableArrayList<Match>("matches")
        matches?.let {
            matchesAdapter.submitList(matches)
        }

        return view
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
}
