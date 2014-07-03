import com.latitude.gpsmaster.tkl.TKLHandler;
import com.latitude.gpsmaster.tkl.TKLTrackPoint;

import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Tkl2Gpx { 
	public static void main(String [] args) { 
		if(args.length < 1) {
			System.err.println("Usage: tkl2gpx <filename>");
			return;
		}
		try {
			String fileName=args[0] + ".gpx";

			//convert the data
			TKLHandler tlk=new TKLHandler(args[0]);
			ArrayList<TKLTrackPoint> trackPtList=tlk.getTrackPointList();

			if(trackPtList.size() == 0) {
				System.out.println("This file is empty");
			}

			//Java bullshit
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("gpx");
			doc.appendChild(rootElement);

			//meta data
			Element meta=doc.createElement("metadata");
			rootElement.appendChild(meta);

			Element rootTime=doc.createElement("time");
			rootTime.appendChild(doc.createTextNode(trackPtList.get(0).getGPXDateTimeString()));
			meta.appendChild(rootTime);

			//GPS trace
			Element trk=doc.createElement("trk");
			rootElement.appendChild(trk);

			Element trkseg=doc.createElement("trkseg");
			trk.appendChild(trkseg);

			for(TKLTrackPoint tp : trackPtList) {
				Element tpt=doc.createElement("trkpt");
				trkseg.appendChild(tpt);

				//Position
				tpt.setAttribute("lat", tp.getLatitude() + "");
				tpt.setAttribute("lon", tp.getLongitude() + "");
				Element ele=doc.createElement("ele");
				ele.appendChild(doc.createTextNode(tp.getAltitude(true) + ""));
				tpt.appendChild(ele);

				//Time
				Element time=doc.createElement("time");
				time.appendChild(doc.createTextNode(tp.getGPXDateTimeString()));
				tpt.appendChild(time);
			}

			//More java bullshit
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
		}
		catch(ParserConfigurationException e) {
			System.err.println("Something went wrong");
		}
		catch(TransformerException e) {
			System.err.println("Something went wrong");
		}
	}
}
