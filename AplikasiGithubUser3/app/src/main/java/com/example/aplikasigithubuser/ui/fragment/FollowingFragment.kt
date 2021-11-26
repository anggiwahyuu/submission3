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
import com.example.aplikasigithubuser.databinding.FragmentFollowingBinding
import com.example.aplikasigithubuser.ui.activity.DetailActivity
import com.example.aplikasigithubuser.ui.controller.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(this)[FollowingViewModel::class.java]

        val arguments = arguments
        username = arguments?.getString(DetailActivity.EXTRA_USER).toString()

        val layoutManager = LinearLayoutManager(activity)
        binding.viewFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.viewFollowing.addItemDecoration(itemDecoration)
        val adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.viewFollowing.adapter = adapter

        showLoading(true)
        model.setListFollowing(username)
        model.getListFollowing().observe(viewLifecycleOwner) {
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
            binding.progressBarFollowing.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowing.visibility = View.GONE
        }
    }
}