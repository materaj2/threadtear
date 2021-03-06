package me.nov.threadtear.analysis.full.value.values;

import java.util.Objects;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.analysis.BasicValue;

import me.nov.threadtear.analysis.full.value.CodeReferenceValue;

public class BinaryOpValue extends CodeReferenceValue {

	public int opcode;
	public CodeReferenceValue left;
	public CodeReferenceValue right;

	public BinaryOpValue(BasicValue type, int opcode, CodeReferenceValue left, CodeReferenceValue right) {
		super(type);
		this.opcode = opcode;
		this.left = Objects.requireNonNull(left);
		this.right = Objects.requireNonNull(right);
	}

	@Override
	public boolean isKnownValue() {
		return left.isKnownValue() && right.isKnownValue();
	}

	@Override
	public CodeReferenceValue combine() {
		if (!isKnownValue()) {
			return this;
		}
		return new NumberValue(type, getStackValueOrNull());
	}

	@Override
	public boolean equalsWith(CodeReferenceValue obj) {
		if (getClass() != obj.getClass())
			return false;
		BinaryOpValue other = (BinaryOpValue) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (opcode != other.opcode)
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

	@Override
	public InsnList toInstructions() {
		InsnList list = new InsnList();
		list.add(left.toInstructions());
		list.add(right.toInstructions());
		list.add(new InsnNode(opcode));
		return list;
	}

	@Override
	public Object getStackValueOrNull() {
		if (!isKnownValue()) {
			return null;
		}
		Number num1 = (Number) left.getStackValueOrNull();
		Number num2 = (Number) right.getStackValueOrNull();
		switch (opcode) {
		case IADD:
			return num1.intValue() + num2.intValue();
		case ISUB:
			return num1.intValue() - num2.intValue();
		case IMUL:
			return num1.intValue() * num2.intValue();
		case IDIV:
			return num1.intValue() / num2.intValue();
		case IREM:
			return num1.intValue() % num2.intValue();
		case ISHL:
			return num1.intValue() << num2.intValue();
		case ISHR:
			return num1.intValue() >> num2.intValue();
		case IUSHR:
			return num1.intValue() >>> num2.intValue();
		case IAND:
			return num1.intValue() & num2.intValue();
		case IOR:
			return num1.intValue() | num2.intValue();
		case IXOR:
			return num1.intValue() ^ num2.intValue();
		case FADD:
			return num1.floatValue() + num2.floatValue();
		case FSUB:
			return num1.floatValue() - num2.floatValue();
		case FMUL:
			return num1.floatValue() * num2.floatValue();
		case FDIV:
			return num1.floatValue() / num2.floatValue();
		case FREM:
			return num1.floatValue() % num2.floatValue();
		case LADD:
			return num1.longValue() + num2.longValue();
		case LSUB:
			return num1.longValue() - num2.longValue();
		case LMUL:
			return num1.longValue() * num2.longValue();
		case LDIV:
			return num1.longValue() / num2.longValue();
		case LREM:
			return num1.longValue() % num2.longValue();
		case LSHL:
			return num1.longValue() << num2.longValue();
		case LSHR:
			return num1.longValue() >> num2.longValue();
		case LUSHR:
			return num1.longValue() >>> num2.longValue();
		case LAND:
			return num1.longValue() & num2.longValue();
		case LOR:
			return num1.longValue() | num2.longValue();
		case LXOR:
			return num1.longValue() ^ num2.longValue();
		case DADD:
			return num1.doubleValue() + num2.doubleValue();
		case DSUB:
			return num1.doubleValue() - num2.doubleValue();
		case DMUL:
			return num1.doubleValue() * num2.doubleValue();
		case DDIV:
			return num1.doubleValue() / num2.doubleValue();
		case DREM:
			return num1.doubleValue() % num2.doubleValue();

		// compare instructions not tested, could return wrong result
		case LCMP:
			return Long.compare(num1.longValue(), num2.longValue());
		case FCMPL:
		case FCMPG:
			// no NaN handling, could affect results
			return Float.compare(num1.longValue(), num2.longValue());
		case DCMPL:
		case DCMPG:
			// no NaN handling, could affect results
			return Double.compare(num1.longValue(), num2.longValue());
		default:
			return null;
		}
	}
}