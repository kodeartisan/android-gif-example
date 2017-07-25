package burrows.apps.example.gif.presentation.adapter

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.rule.UiThreadTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.ViewGroup
import android.widget.LinearLayout
import burrows.apps.example.gif.data.rest.repository.ImageApiRepository
import burrows.apps.example.gif.presentation.adapter.GifAdapter.OnItemClickListener
import burrows.apps.example.gif.presentation.adapter.model.ImageInfoModel
import com.nhaarman.mockito_kotlin.eq
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import test.AndroidTestBase

/**
 * @author [Jared Burrows](mailto:jaredsburrows@gmail.com)
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
class GifAdapterTest : AndroidTestBase() {
  @get:Rule val uiThreadTestRule = UiThreadTestRule()
  private val targetContext: Context = InstrumentationRegistry.getTargetContext()
  private val imageInfoModel = ImageInfoModel().apply { url = STRING_UNIQUE }
  private val imageInfoModel2 = ImageInfoModel().apply { url = STRING_UNIQUE2 }
  private val imageInfoModel3 = ImageInfoModel().apply { url = STRING_UNIQUE3 }
  @Mock private lateinit var onItemClickListener: OnItemClickListener
  private lateinit var viewHolder: GifAdapter.ViewHolder
  private lateinit var imageApiRepository: ImageApiRepository
  private lateinit var sut: GifAdapter

  @Before override fun setUp() {
    super.setUp()

    initMocks(this)

    imageApiRepository = spy(ImageApiRepository(targetContext))
    sut = GifAdapter(onItemClickListener, imageApiRepository)
    sut.add(imageInfoModel)
    sut.add(imageInfoModel2)
    // Must be created on UI thread
    uiThreadTestRule.runOnUiThread { viewHolder = sut.onCreateViewHolder(LinearLayout(targetContext), 0) }
  }

  @Test fun testOnCreateViewHolder() {
    // Arrange
    val parent = object : ViewGroup(targetContext) {
      override fun onLayout(b: Boolean, i: Int, i1: Int, i2: Int, i3: Int) {}
    }

    // Assert
    // Must be created on UI thread
    uiThreadTestRule.runOnUiThread { assertThat(sut.onCreateViewHolder(parent, 0)).isInstanceOf(GifAdapter.ViewHolder::class.java) }
  }

  @Test fun testOnBindViewHolderOnAdapterItemClick() {
    // Arrange
    sut.clear()
    sut.add(imageInfoModel)
    sut.add(imageInfoModel2)
    sut.add(ImageInfoModel())

    // Act
    uiThreadTestRule.runOnUiThread { sut.onBindViewHolder(viewHolder, 0) }

    // Assert
    assertThat(viewHolder.itemView.performClick()).isTrue()
    verify(imageApiRepository, atLeastOnce()).load(eq(STRING_UNIQUE))
    verify(onItemClickListener).onClick(eq(imageInfoModel))
  }

  @Test fun testGetItem() {
    // Arrange
    sut.clear()

    // Act
    val imageInfo = ImageInfoModel()
    sut.add(imageInfo)

    // Assert
    assertThat(sut.getItem(0)).isEqualTo(imageInfo)
  }

  @Test fun onViewRecycled() {
    // Arrange
    sut.add(ImageInfoModel())

    // Act
    uiThreadTestRule.runOnUiThread {
      sut.onBindViewHolder(viewHolder, 0)
      sut.onViewRecycled(viewHolder)
    }
  }

  @Test fun testGetItemCountShouldReturnCorrectValues() {
    assertThat(sut.itemCount).isEqualTo(2)
  }

  @Test fun testGetListCountShouldReturnCorrectValues() {
    assertThat(sut.getItem(0)).isEqualTo(imageInfoModel)
    assertThat(sut.getItem(1)).isEqualTo(imageInfoModel2)
  }

  @Test fun testGetItemShouldReturnCorrectValues() {
    assertThat(sut.getItem(1)).isEqualTo(imageInfoModel2)
  }

  @Test fun testGetLocationShouldReturnCorrectValues() {
    assertThat(sut.getLocation(imageInfoModel2)).isEqualTo(1)
  }

  @Test fun testClearShouldClearAdapter() {
    // Act
    sut.clear()

    // Assert
    assertThat(sut.itemCount).isEqualTo(0)
  }

  @Test fun testAddObjectShouldReturnCorrectValues() {
    // Act
    sut.add(imageInfoModel3)

    // Assert
    assertThat(sut.getItem(0)).isEqualTo(imageInfoModel)
    assertThat(sut.getItem(1)).isEqualTo(imageInfoModel2)
    assertThat(sut.getItem(2)).isEqualTo(imageInfoModel3)
  }

  @Test fun testAddCollectionShouldReturnCorrectValues() {
    // Arrange
    val imageInfos = listOf(imageInfoModel3)

    // Act
    sut.addAll(imageInfos)

    // Assert
    assertThat(sut.getItem(0)).isEqualTo(imageInfoModel)
    assertThat(sut.getItem(1)).isEqualTo(imageInfoModel2)
    assertThat(sut.getItem(2)).isEqualTo(imageInfoModel3)
  }

  @Test fun testAddLocationObjectShouldReturnCorrectValues() {
    // Act
    sut.add(0, imageInfoModel3)

    // Assert
    assertThat(sut.getItem(0)).isEqualTo(imageInfoModel3)
    assertThat(sut.getItem(1)).isEqualTo(imageInfoModel)
    assertThat(sut.getItem(2)).isEqualTo(imageInfoModel2)
  }

  @Test fun testRemoveLocationObjectShouldReturnCorrectValues() {
    // Act
    sut.remove(0, imageInfoModel)

    // Assert
    assertThat(sut.getItem(0)).isEqualTo(imageInfoModel2)
  }

  @Test fun testRemoveObjectShouldReturnCorrectValues() {
    // Act
    sut.remove(imageInfoModel)

    // Assert
    assertThat(sut.getItem(0)).isEqualTo(imageInfoModel2)
  }

  @Test fun testRemoveLocationShouldReturnCorrectValues() {
    // Act
    sut.remove(0)

    // Assert
    assertThat(sut.getItem(0)).isEqualTo(imageInfoModel2)
  }
}