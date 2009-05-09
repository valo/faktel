package com.faktel.scrapped_code.filters;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import com.faktel.InvoiceRow;
import com.faktel.Utils;
import com.faktel.filters.RowFilter;

/**
 * Filter which can aggregate row data values.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public abstract class AggregateRowFilter implements RowFilter {
	private static final Logger logger = Logger
			.getLogger(com.faktel.scrapped_code.filters.AggregateRowFilter.class.getName());
	private static final String GROUP = "group";
	private static final String MAX = "max";
	private static final String MIN = "min";
	private static final String AVG = "avg";
	private static final String SUM = "sum";
	private static final String COUNT = "count";
	private static final String NOFLATABLE = "no_flat";

	private Collection<String> m_groupable;
	private Collection<String> m_maximizable;
	private Collection<String> m_minimizable;
	private Collection<String> m_averageble;
	private Collection<String> m_summable;
	private Collection<String> m_countable;
	private boolean m_notFlatable;

	public AggregateRowFilter(Map<String, Collection<String>> args) {
		m_groupable = args.get(GROUP);
		if (m_groupable == null)
			m_groupable = new LinkedList<String>();
		m_maximizable = args.get(MAX);
		if (m_maximizable == null)
			m_maximizable = new LinkedList<String>();
		m_minimizable = args.get(MIN);
		if (m_minimizable == null)
			m_minimizable = new LinkedList<String>();
		m_averageble = args.get(AVG);
		if (m_averageble == null)
			m_averageble = new LinkedList<String>();
		m_summable = args.get(SUM);
		if (m_summable == null)
			m_summable = new LinkedList<String>();
		m_countable = args.get(COUNT);
		if (m_countable == null)
			m_countable = new LinkedList<String>();
		Collection<String> noFlat = args.get(NOFLATABLE);
		m_notFlatable = noFlat != null && noFlat.size() > 0 ? Boolean
				.parseBoolean(noFlat.iterator().next()) : false;
	}

	/**
	 * Sum of all fields
	 * 
	 * @param col
	 *            All fields
	 * @param f
	 *            Sum by this field
	 * @return Sum
	 */
	private Object sum(Collection<InvoiceRow> col, Field f) {
		Double sum = 0.0;
		for (InvoiceRow r : col) {
			try {
				Number num = Utils.takeNumberValue(f.get(r));
				if (num != null)
					sum += num.doubleValue();
			} catch (Exception e) {
				logger.severe("Field " + f + " is not accessible");
			}
		}
		return sum;
	}

	/**
	 * Count of all fields
	 * 
	 * @param col
	 *            All fields
	 * @param f
	 *            Sum by this field
	 * @return Count
	 */
	private Object count(Collection<InvoiceRow> col, Field f) {
		return col.size();
	}

	/**
	 * Average of all fields
	 * 
	 * @param col
	 *            All fields
	 * @param f
	 *            Sum by this field
	 * @return Average
	 */
	private Object avg(Collection<InvoiceRow> col, Field f) {
		int count = (Integer) count(col, f);
		return count == 0 ? 0 : (Double) sum(col, f) / count;
	}

	/**
	 * Minimum of all fields
	 * 
	 * @param col
	 *            All fields
	 * @param f
	 *            Sum by this field
	 * @return Minimum
	 */
	private Object min(Collection<InvoiceRow> col, Field f) {
		Double min = Double.MAX_VALUE;
		for (InvoiceRow r : col) {
			try {
				Number num = Utils.takeNumberValue(f.get(r));
				if (num != null && min > num.doubleValue())
					min = num.doubleValue();
			} catch (Exception e) {
				logger.severe("Field " + f + " is not accessible");
			}
		}
		return min == Double.MAX_VALUE ? 0 : min;
	}

	/**
	 * Maximum of all fields
	 * 
	 * @param col
	 *            All fields
	 * @param f
	 *            Sum by this field
	 * @return Maximum
	 */
	private Object max(Collection<InvoiceRow> col, Field f) {
		Double max = Double.MIN_VALUE;
		for (InvoiceRow r : col) {
			try {
				Number num = Utils.takeNumberValue(f.get(r));
				if (num != null && max < num.doubleValue())
					max = num.doubleValue();
			} catch (Exception e) {
				logger.severe("Field " + f + " is not accessible");
			}
		}
		return max == Double.MIN_VALUE ? 0 : max;
	}

	/**
	 * Aggregate data field
	 * 
	 * @param r
	 *            Result row data
	 * @param col
	 *            Collection of values
	 */
	private void aggragate(InvoiceRow r, Collection<InvoiceRow> col,
			boolean takeDefaultValue) {
		for (Field f : r.getClass().getDeclaredFields()) {
			if (!f.isAccessible())
				f.setAccessible(true);
			String name = f.getName();

			try {
				if (m_maximizable.contains(name)) {
					f.set(r, max(col, f));
				} else if (m_minimizable.contains(name)) {
					f.set(r, min(col, f));
				} else if (m_averageble.contains(name)) {
					f.set(r, avg(col, f));
				} else if (m_summable.contains(name)) {
					f.set(r, sum(col, f));
				} else if (m_countable.contains(name)) {
					f.set(r, count(col, f));
				} else if (takeDefaultValue) {
					f.set(r, f.get(col.iterator().next()));
				}
			} catch (Exception e) {
				logger.severe("Field " + f + " is not accessible");
			}
		}
	}

	/**
	 * Evaluate given tree to flat table
	 * 
	 * @param tree
	 *            Evaluation tree
	 * @param res
	 *            Pointer to result collection
	 */
	private void evaluate(TreeHelper tree, Collection<InvoiceRow> res) {
		for (TreeHelper ch : tree.m_children) {
			evaluate(ch, res);
		}

		// flat given collection
		Collection<InvoiceRow> col = tree.m_data;
		if (col != null && col.size() > 0) {
			if (m_notFlatable) {
				for (InvoiceRow r : col) {
					aggragate(r, col, false);
				}
				res.addAll(col);
			} else {
				InvoiceRow r = new InvoiceRow();
				aggragate(r, col, true);
				res.add(r);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Object execute(Collection<InvoiceRow> original, File workingDir,
			Object referenced, Object specificParams) {
		if (!(referenced instanceof Collection))
			return null;
		Collection<InvoiceRow> refData = (Collection<InvoiceRow>) referenced;
		TreeHelper helper = TreeHelper.createRoot();
		if (m_groupable.size() > 0) {
			Collection<Field> group = Utils.precomputeFields(m_groupable,
					InvoiceRow.class);

			for (InvoiceRow data : refData) {
				TreeHelper root = helper;
				for (Field f : group) {
					// TODO possible NullPointerException.
					String value = "null";
					try {
						Object o = f.get(data);
						if (o != null)
							value = o.toString();
						root = root.createChild(value);
					} catch (Exception e) {
						logger.info("Field " + f
								+ " is not accessible. Exception: " + e);
					}
				}

				root.addValue(data);
			}
		} else {
			helper.m_data = refData;
		}

		Collection<InvoiceRow> res = new LinkedList<InvoiceRow>();
		evaluate(helper, res);
		return res;
	}

	/**
	 * Simple tree implementation.
	 * 
	 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
	 * @version 1.0
	 */
	private static class TreeHelper {
		private TreeHelper m_parent;
		private String m_name;
		private Collection<TreeHelper> m_children;
		private Collection<InvoiceRow> m_data;

		public TreeHelper(String name) {
			this(null, name, null);
		}

		public TreeHelper(TreeHelper parent, String name,
				Collection<TreeHelper> children) {
			this(parent, name, children, null);
		}

		public TreeHelper(TreeHelper parent, String name,
				Collection<TreeHelper> children, Collection<InvoiceRow> data) {
			m_parent = parent;
			m_name = name;
			m_children = children != null ? children
					: new LinkedList<TreeHelper>();
			m_data = data;

			if (parent != null) {
				parent.m_children.add(this);
			}
		}

		public static TreeHelper createRoot() {
			return new TreeHelper(null, "root", null);
		}

		public boolean isRoot() {
			return m_parent == null;
		}

		public boolean hasChildren() {
			return m_children.size() > 0;
		}

		public TreeHelper createChild(String name) {
			for (TreeHelper ch : m_children)
				if (ch.m_name.equals(name))
					return ch;

			return new TreeHelper(this, name, null);
		}

		public void addValue(InvoiceRow row) {
			if (m_data == null)
				m_data = new LinkedList<InvoiceRow>();
			m_data.add(row);
		}
	}
}
