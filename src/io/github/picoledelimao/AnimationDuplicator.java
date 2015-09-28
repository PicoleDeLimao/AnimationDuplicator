package io.github.picoledelimao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import io.github.picoledelimao.mdl.MDLAnim;
import io.github.picoledelimao.mdl.MDLAnimatedObject;
import io.github.picoledelimao.mdl.MDLAnimationKey;
import io.github.picoledelimao.mdl.MDLAnimationKeys;
import io.github.picoledelimao.mdl.MDLAttachment;
import io.github.picoledelimao.mdl.MDLBone;
import io.github.picoledelimao.mdl.MDLGeosetAnim;
import io.github.picoledelimao.mdl.MDLHelper;
import io.github.picoledelimao.mdl.MDLLayer;
import io.github.picoledelimao.mdl.MDLLight;
import io.github.picoledelimao.mdl.MDLMaterial;
import io.github.picoledelimao.mdl.MDLModel;
import io.github.picoledelimao.mdl.MDLNode;
import io.github.picoledelimao.mdl.MDLParticleEmitter;
import io.github.picoledelimao.mdl.MDLParticleEmitter2;
import io.github.picoledelimao.mdl.MDLRibbonEmitter;
import io.github.picoledelimao.mdl.core.MDLNotFoundException;
import io.github.picoledelimao.mdl.core.MDLParserErrorException;

public class AnimationDuplicator {

	public static int getLastInterval(List<MDLAnim> anims) {
		int lastInterval = 0;
		for (MDLAnim anim : anims) {
			if (anim.getInterval()[1] > lastInterval) {
				lastInterval = Math.round(anim.getInterval()[1]);
			}
		}
		return lastInterval;
	}
	
	public static MDLAnim cloneAnimation(MDLAnim anim) throws MDLNotFoundException, MDLParserErrorException {
		String mdl = anim.toMDL();
		MDLAnim clonedAnim = new MDLAnim();
		clonedAnim.parse(mdl);
		return clonedAnim;
	}
	
	public static MDLAnim getAlternateAnim(MDLAnim anim, String animationTag, int lastInterval) throws MDLNotFoundException, MDLParserErrorException {
		MDLAnim alternateAnim = cloneAnimation(anim);
		String animName = alternateAnim.getValue().replaceAll("\"", "");
		String[] animNames = animName.split("-");
		String newAnimName = animNames[0] + " " + animationTag;
		if (animNames.length > 1) {
			newAnimName += " - " + animNames[1];;
		}
		newAnimName = newAnimName.replaceAll("  ", " ");
		alternateAnim.setValue("\"" + newAnimName + "\"");
		alternateAnim.setInterval(new Float[]{ alternateAnim.getInterval()[0] + lastInterval, alternateAnim.getInterval()[1] + lastInterval });
		return alternateAnim;
	}
	
	public static MDLAnimationKey cloneKey(MDLAnimationKey key) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MDLNotFoundException, MDLParserErrorException {
		String mdl = key.toMDL();
		MDLAnimationKey clonedKey = new MDLAnimationKey<>(key.isShowTanValue(), key.getConstructor(), key.getConstructorParams());
		clonedKey.parse(mdl);
		return clonedKey;
	}
	
	public static MDLAnimationKey getAlternateKey(MDLAnimationKey key, int lastInterval) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MDLNotFoundException, MDLParserErrorException {
		MDLAnimationKey alternateKey = cloneKey(key);
		alternateKey.setKey(alternateKey.getKey() + lastInterval);
		alternateKey.getValue().setName(alternateKey.getKey() + ":");
		return alternateKey;
	}
	
	public static void cloneAnimations(MDLAnimatedObject obj, int lastInterval) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MDLNotFoundException, MDLParserErrorException {
		Object value = obj.getValue();
		if (value instanceof MDLAnimationKeys) {
			MDLAnimationKeys valueKeys = (MDLAnimationKeys) value;
			List<MDLAnimationKey> keys = valueKeys.getObjects();
			int size = keys.size();
			for (int i = 0; i < size; i++) {
				keys.add(getAlternateKey(keys.get(i), lastInterval));
			}
		} 
	}
	
	public static void cloneAnimations(MDLNode node, int lastInterval) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MDLNotFoundException, MDLParserErrorException {
		cloneAnimations(node.getRotation(), lastInterval);
		cloneAnimations(node.getScaling(), lastInterval);
		cloneAnimations(node.getTranslation(), lastInterval);
	}
	
	public static void cloneAnimations(String inputFile, String outputFile, String animationTag, boolean cloneMaterials, boolean cloneParticleEmitters) throws IOException, MDLNotFoundException, MDLParserErrorException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		System.out.println("Reading from file...");
		MDLModel model = MDLModel.parseFromFile(inputFile);
		System.out.println("Cloning animations...");
		List<MDLAnim> anims = model.getSequences().getObjects();
		int lastInterval = getLastInterval(anims) + 100;
		int size = anims.size();
		for (int i = 0; i < size; i++) {
			MDLAnim anim = anims.get(i);
			anims.add(getAlternateAnim(anim, animationTag, lastInterval));
		}
		for (MDLBone bone : model.getBones()) {
			cloneAnimations(bone, lastInterval);
		}
		for (MDLHelper helper : model.getHelpers()) {
			cloneAnimations(helper, lastInterval);
		}
		for (MDLAttachment attachment : model.getAttachments()) {
			cloneAnimations(attachment, lastInterval);
		}
		for (MDLLight light : model.getLights()) {
			cloneAnimations(light, lastInterval);
		}
		for (MDLGeosetAnim anim : model.getGeosetAnims()) {
			cloneAnimations(anim.getAlpha(), lastInterval);
			cloneAnimations(anim.getColor(), lastInterval);
		}
		if (cloneMaterials) {
			System.out.println("Cloning materials...");
			for (MDLMaterial material : model.getMaterials().getObjects()) {
				for (MDLLayer layer : material.getObjects()) {
					cloneAnimations(layer.getAlpha(), lastInterval);
					cloneAnimations(layer.getTextureID(), lastInterval);
				}
			}
		}
		if (cloneParticleEmitters) {
			System.out.println("Cloning particle emitters...");
			for (MDLParticleEmitter emitter : model.getParticleEmitters()) {
				cloneAnimations(emitter, lastInterval);
				cloneAnimations(emitter.getEmissionRate(), lastInterval);
				cloneAnimations(emitter.getGravity(), lastInterval);
				cloneAnimations(emitter.getLatitude(), lastInterval);
				cloneAnimations(emitter.getLongitude(), lastInterval);
				cloneAnimations(emitter.getVisibility(), lastInterval);
				if (emitter.getParticle() != null) {
					cloneAnimations(emitter.getParticle().getLifeSpan(), lastInterval);
					cloneAnimations(emitter.getParticle().initVelocity(), lastInterval);
				}
			}
			for (MDLParticleEmitter2 emitter : model.getParticleEmitters2()) {
				cloneAnimations(emitter, lastInterval);
				cloneAnimations(emitter.getEmissionRate(), lastInterval);
				cloneAnimations(emitter.getGravity(), lastInterval);
				cloneAnimations(emitter.getLatitude(), lastInterval);
				cloneAnimations(emitter.getLength(), lastInterval);
				cloneAnimations(emitter.getSpeed(), lastInterval);
				cloneAnimations(emitter.getVariation(), lastInterval);
				cloneAnimations(emitter.getVisibility(), lastInterval);
				cloneAnimations(emitter.getWidth(), lastInterval);
			}
			for (MDLRibbonEmitter emitter : model.getRibbonEmitters()) {
				cloneAnimations(emitter, lastInterval);
				cloneAnimations(emitter.getAlpha(), lastInterval);
				cloneAnimations(emitter.getColor(), lastInterval);
				cloneAnimations(emitter.getHeightAbove(), lastInterval);
				cloneAnimations(emitter.getHeightBelow(), lastInterval);
				cloneAnimations(emitter.getTextureSlot(), lastInterval);
				cloneAnimations(emitter.getVisibility(), lastInterval);
			}
		}
		System.out.println("Writing to file...");
		model.writeToFile(outputFile);
		System.out.println("Done.");
	}
	
	public static void main(String[] args) {
		try {
			cloneAnimations("C:\\Users\\Abner Matheus\\Desktop\\Juugo.mdl", "C:\\Users\\Abner Matheus\\Desktop\\Juugo2.mdl", "Alternate", true, true);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IOException | MDLNotFoundException | MDLParserErrorException e1) {
			e1.printStackTrace();
		}
	}
	
}
