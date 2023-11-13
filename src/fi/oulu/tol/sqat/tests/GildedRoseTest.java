package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {
	
	@Test
	public void testTheTruth() {
		assertTrue(true);
	}
	
	@Test
	public void testMainValues() {
		List<Item>items = new ArrayList<Item>();
        items.add(new Item("+5 Dexterity Vest", 10, 20));
        items.add(new Item("Aged Brie", 2, 0));
        items.add(new Item("Elixir of the Mongoose", 5, 7));
        items.add(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
        items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
        items.add(new Item("Conjured Mana Cake", 3, 6));
        
		GildedRose.updateQuality(items);
		
		assertEquals(19, items.get(0).getQuality());
	    assertEquals(1, items.get(1).getQuality());
	    assertEquals(6, items.get(2).getQuality());
	    assertEquals(80, items.get(3).getQuality());
	    assertEquals(21, items.get(4).getQuality());
	    assertEquals(4, items.get(5).getQuality());
	}
	
	@Test
	public void testSellDateQualityDegradation() {
	    List<Item> items = new ArrayList<Item>();
	    items.add(new Item("item1", 1, 5));
	    GildedRose.updateQuality(items);
	    assertEquals(4, items.get(0).getQuality());
	}

	@Test
	public void testQualityIsNeverNegative() {
	    List<Item> items = new ArrayList<Item>();
	    items.add(new Item("item1", 10, 5));
	    items.add(new Item("item2", 5, 0));
	    items.add(new Item("item3", -1, 10));
	    items.add(new Item("item4", 5, -1));
	    items.add(new Item("item5", -1, -1));
	    
	    GildedRose.updateQuality(items);
	    
	    assertEquals(4, items.get(0).getQuality());
	    assertEquals(0, items.get(1).getQuality());
	    assertEquals(8, items.get(2).getQuality());
	    assertEquals(0, items.get(3).getQuality());
	    assertEquals(0, items.get(4).getQuality());
	}
	
	@Test
	public void testSulfurasNeverDecreasesInQualityOrSellIn() {
	    List<Item> items = new ArrayList<Item>();
	    items.add(new Item("Sulfuras, Hand of Ragnaros", 5, 80));
	    items.add(new Item("Sulfuras, Hand of Ragnaros", -1, 80));
	    items.add(new Item("Sulfuras, Hand of Ragnaros", -1, -1));
	    GildedRose.updateQuality(items);
	    assertEquals(80, items.get(0).getQuality());
	    assertEquals(5, items.get(0).getSellIn());
	    assertEquals(80, items.get(1).getQuality());
	    assertEquals(-1, items.get(1).getSellIn());
	    assertEquals(80, items.get(2).getQuality());
	}
	
	@Test
	public void testAgedBrie() {
	    List<Item> items = new ArrayList<Item>();
	    items.add(new Item("Aged Brie", 2, 10));
	    items.add(new Item("Aged Brie", -1, 10));
	    items.add(new Item("Aged Brie", -1, 49));
	    GildedRose.updateQuality(items);
	    assertEquals(11, items.get(0).getQuality());
	    assertEquals(12, items.get(1).getQuality());
	    assertEquals(50, items.get(2).getQuality());
	}
	
	@Test
	public void testConjuredManaCake() {
		List<Item> items = new ArrayList<Item>();
	    items.add(new Item("Conjured Mana Cake", 2, 10));
	    items.add(new Item("Conjured Mana Cake", -1, 10));
	    items.add(new Item("Conjured Mana Cake", 0, 50));
	    GildedRose.updateQuality(items);
	    assertEquals(8, items.get(0).getQuality());
	    assertEquals(6, items.get(1).getQuality());
	    assertEquals(46, items.get(2).getQuality());
	}
	
	@Test
	public void testBackstagePasses() {
	    List<Item> items = new ArrayList<Item>();
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 12, 20));
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 7, 20));
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 2, 20));
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20));
	    
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 12, 50));
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 7, 50));
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 2, 50));
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 0, 50));
	    
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", 2, 51));
	    items.add(new Item("Backstage passes to a TAFKAL80ETC concert", -1, 51));
	    
	    GildedRose.updateQuality(items);
	    
	    assertEquals(21, items.get(0).getQuality()); // 12 days left, increase by 1
	    assertEquals(22, items.get(1).getQuality()); // 7 days left, increase by 2
	    assertEquals(23, items.get(2).getQuality()); // 2 days left, increase by 3
	    assertEquals(0, items.get(3).getQuality()); // After the concert, quality drops to 0
	    
	    assertEquals(50, items.get(4).getQuality()); // Test max quality, 4 cases
	    assertEquals(50, items.get(5).getQuality());
	    assertEquals(50, items.get(6).getQuality());
	    assertEquals(0, items.get(7).getQuality());
	    
	    assertEquals(50, items.get(8).getQuality());
	    assertEquals(0, items.get(9).getQuality());
	}
}
