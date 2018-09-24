package interventure.com.androidtv

import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.SearchSupportFragment
import android.support.v17.leanback.widget.*
import android.support.v4.app.ActivityOptionsCompat
import android.text.TextUtils

class MySearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {
    private var mRowsAdapter: ArrayObjectAdapter? = null
    private val mCardPresenter = CardPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        setSearchResultProvider(this)
        setOnItemViewClickedListener(ItemViewClickedListener())
    }

    override fun getResultsAdapter(): ObjectAdapter? {
        return mRowsAdapter
    }

    override fun onQueryTextChange(newQuery: String): Boolean {
        mRowsAdapter!!.clear()
        if (!TextUtils.isEmpty(newQuery)) {
            mRowsAdapter?.clear()
            val header = HeaderItem(1, "Search result")
            mRowsAdapter?.add(ListRow(header, searchInMovies(newQuery)))
        }
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        mRowsAdapter!!.clear()
        if (!TextUtils.isEmpty(query)) {
            mRowsAdapter?.clear()
            val header = HeaderItem(1, "Search result")
            mRowsAdapter?.add(ListRow(header, searchInMovies(query)))
        }
        return true
    }

    private fun searchInMovies(searchQuery: String): ArrayObjectAdapter {
        val listRowAdapter = ArrayObjectAdapter(mCardPresenter)
        for (movie in MovieList.list) {
            if (movie.title?.contains(searchQuery, true)!!
                    || movie.studio?.contains(searchQuery, true)!!) {
                listRowAdapter.add(movie)
            }
        }
        return listRowAdapter
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
                itemViewHolder: Presenter.ViewHolder?,
                item: Any?,
                rowViewHolder: RowPresenter.ViewHolder,
                row: Row) {
            if (item is Movie) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra(resources.getString(R.string.movie), item)

                val bundle =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                activity!!,
                                (itemViewHolder?.view as ImageCardView).mainImageView,
                                DetailsActivity.SHARED_ELEMENT_NAME)
                                .toBundle()
                activity?.startActivity(intent, bundle)
            }
        }
    }
}