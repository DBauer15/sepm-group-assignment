package at.ac.tuwien.sepm.assignment.groupphase.application.util.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import javax.activation.MimetypesFileTypeMap;

import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationContext;
import at.ac.tuwien.sepm.assignment.groupphase.application.service.ServiceInvokationException;

public class ImageUtil {
	private ImageUtil() {
	}

	private static final String[] allowedMimeTypes = { "image/png", "image/jpeg", "image/jpg" };

	public static String getImageType(File f) throws ServiceInvokationException {
		try {
			String contentType = Files.probeContentType(f.toPath()).toLowerCase();

			if (!Arrays.asList(allowedMimeTypes).contains(contentType)) {
				ServiceInvokationContext context = new ServiceInvokationContext();
				context.addError("You have selected an invalid file. Only JPEG, JPG or PNG files are allowed.");

				throw new ServiceInvokationException(context);
			} else {
				return contentType.replace("image/", "");
			}
		} catch (IOException ex) {
			throw new ServiceInvokationException(ex);
		}
	}
}