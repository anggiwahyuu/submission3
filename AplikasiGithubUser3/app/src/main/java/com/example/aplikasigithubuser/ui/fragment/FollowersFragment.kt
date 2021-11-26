package com.example.aplikasigithubuser.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.adapter.UserAdapter
import com.example.aplikasigithubuser.databinding.FragmentFollowersBinding
import com.example.aplikasigithubuser.ui.activity.DetailActivity
import com.example.aplikasigithubuser.ui.controller.FollowersViewModel

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(this)[FollowersViewModel::class.java]

        val arguments = arguments
        username = arguments?.getString(DetailActivity.EXTRA_USER).toString()

        val layoutManager = LinearLayoutManager(activity)
        binding.viewFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.viewFollowers.addItemDecoration(itemDecoration)
        val adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.viewFollowers.adapter = adapter

        showLoading(true)
        model.setListFollowers(username)
        model.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollowers.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowers.visibility = View.GONE
        }
    }
}