<?xml version="1.0" encoding="iso-8859-1"?>
<er:EuroRoadSDataset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="
http://www.euroroads.org C:\Projekt\Vägverket\Euroroads\Leveransobjekt\GML\EuroRoadS.xsd"
  xmlns:gml2="http://www.opengis.net/gml" xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:er="
http://www.euroroads.org">
  <gml2:featureMember>
    <er:RoadNode>
      <er:id>
        <er:permanentId>169d9fa8-4de4-466e-9555-e0b0948c28b6</er:permanentId>
      </er:id>
      <er:formOfNode>Junction</er:formOfNode>
      <er:node>
        <er:Node gml2:id="i1">
          <gml2:pointProperty>
            <gml2:Point>
              <gml2:coord>
                <gml2:X>123.456</gml2:X>
                <gml2:Y>123.456</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
            </gml2:Point>
          </gml2:pointProperty>
        </er:Node>
      </er:node>
    </er:RoadNode>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:RoadNode>
      <er:id>
        <er:permanentId>98a5a365-837a-4be4-9d9a-a261c6c5bb08</er:permanentId>
      </er:id>
      <er:attributes>
        <er:NeighbourBorderNodeInfo>
          <er:borderNodeType>NationalBorder</er:borderNodeType>
          <er:neighbourDatasetId>NOR</er:neighbourDatasetId>
          <er:neighbourId>
            <er:permanentId>7bcf86f7-8a42-4157-9d7f-f590a3ef159e</er:permanentId>
          </er:neighbourId>
        </er:NeighbourBorderNodeInfo>
      </er:attributes>
      <er:formOfNode>Junction</er:formOfNode>
      <er:node>
        <er:Node gml2:id="i2">
          <gml2:pointProperty>
            <gml2:Point>
              <gml2:coord>
                <gml2:X>234.567</gml2:X>
                <gml2:Y>234.567</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
            </gml2:Point>
          </gml2:pointProperty>
        </er:Node>
      </er:node>
    </er:RoadNode>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:RoadLink gml2:id="i3">
      <er:id>
        <er:permanentId>95e6e70a-ff03-4a53-a21e-8135e0624694</er:permanentId>
      </er:id>
      <er:attributes>
        <er:LinkLevel>
          <er:startLevel>1</er:startLevel>
        </er:LinkLevel>
        <er:DirectionOfFlow>
          <er:trafficFlow>Positive</er:trafficFlow>
        </er:DirectionOfFlow>
        <er:RestrictionForVehiclesDir>
          <er:restriction>
            <er:vehicleType>TrolleyBus</er:vehicleType>
            <er:measure uom="meter">5.56</er:measure>
            <er:restrictionType>MaximumHeight</er:restrictionType>
          </er:restriction>
          <er:validityDirection>+</er:validityDirection>
        </er:RestrictionForVehiclesDir>
        <er:SpeedLimit>
          <er:speedLimit>
            <er:speedLimit>70</er:speedLimit>
            <er:speedLimitUnit>km/h</er:speedLimitUnit>
            <er:speedLimitType>Maximum</er:speedLimitType>
          </er:speedLimit>
        </er:SpeedLimit>
      </er:attributes>
      <er:edge>
        <er:Edge gml2:id="i4">
          <gml2:directedNode orientation="-"
            xlink:href="#//er:RoadNode[er:id/er:permanentId=&quot;169d9fa8-4de4-466e-9555-
e0b0948c28b6&quot;]/er:node/er:Node" />
          <gml2:directedNode orientation="+"
            xlink:href="#//er:RoadNode[er:id/er:permanentId=&quot;98a5a365-837a-4be4-9d9aa261c6c5bb08&quot;]/er:node/er:Node" />
          <gml2:curveProperty>
            <gml2:LineString>
              <gml2:coord>
                <gml2:X>123.456</gml2:X>
                <gml2:Y>123.456</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
              <gml2:coord>
                <gml2:X>234.567</gml2:X>
                <gml2:Y>234.567</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
            </gml2:LineString>
          </gml2:curveProperty>
          <er:roadnetLink xlink:href="#i3" />
        </er:Edge>
      </er:edge>
      <er:formOfWay>SingleCarriageway</er:formOfWay>
      <er:functionalRoadClass>FirstClass</er:functionalRoadClass>
    </er:RoadLink>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:RoadNode>
      <er:id>
        <er:permanentId>138dc6d4-f47e-43cc-aa54-08876b72c0d0</er:permanentId>
      </er:id>
      <er:formOfNode>GradeSeparatedCrossing</er:formOfNode>
      <er:node>
        <er:Node gml2:id="i5">
          <gml2:pointProperty>
            <gml2:Point>
              <gml2:coord>
                <gml2:X>34.567</gml2:X>
                <gml2:Y>34.567</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
            </gml2:Point>
          </gml2:pointProperty>
        </er:Node>
      </er:node>
    </er:RoadNode>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:RoadLink gml2:id="i6">
      <er:id>
        <er:permanentId>b57448ea-a5b1-498d-8a18-ddac05f37da6</er:permanentId>
      </er:id>
      <er:attributes>
        <er:LinkLevel>
          <er:endLevel>1</er:endLevel>
        </er:LinkLevel>
      </er:attributes>
      <er:edge>
        <er:Edge gml2:id="i7">
          <gml2:directedNode orientation="-"
            xlink:href="#//er:RoadNode[er:id/er:permanentId=&quot;138dc6d4-f47e-43cc-aa54-
08876b72c0d0&quot;]/er:node/er:Node" />
          <gml2:directedNode orientation="+"
            xlink:href="#//er:RoadNode[er:id/er:permanentId=&quot;169d9fa8-4de4-466e-9555-
e0b0948c28b6&quot;]/er:node/er:Node" />
          <gml2:curveProperty>
            <gml2:LineString>
              <gml2:coord>
                <gml2:X>34.567</gml2:X>
                <gml2:Y>34.567</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
              <gml2:coord>
                <gml2:X>123.456</gml2:X>
                <gml2:Y>123.456</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
            </gml2:LineString>
          </gml2:curveProperty>
          <er:roadnetLink xlink:href="#i6" />
        </er:Edge>
      </er:edge>
      <er:formOfWay>SingleCarriageway</er:formOfWay>
      <er:functionalRoadClass>FirstClass</er:functionalRoadClass>
    </er:RoadLink>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:RoadNode>
      <er:id>
        <er:permanentId>42759a40-4664-48ad-830d-114d0edcf8f7</er:permanentId>
      </er:id>
      <er:formOfNode>Junction</er:formOfNode>
      <er:node>
        <er:Node gml2:id="i8">
          <gml2:pointProperty>
            <gml2:Point>
              <gml2:coord>
                <gml2:X>123.456</gml2:X>
                <gml2:Y>34.567</gml2:Y>
                <gml2:Z>105</gml2:Z>
              </gml2:coord>
            </gml2:Point>
          </gml2:pointProperty>
        </er:Node>
      </er:node>
    </er:RoadNode>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:RoadLink gml2:id="i9">
      <er:id>
        <er:permanentId>7f18a15d-e245-44c8-8bba-3ae9739a6142</er:permanentId>
      </er:id>
      <er:attributes>
        <er:LinkLevel>
          <er:endLevel>1</er:endLevel>
        </er:LinkLevel>
      </er:attributes>
      <er:edge>
        <er:Edge gml2:id="i10">
          <gml2:directedNode orientation="-"
            xlink:href="#//er:RoadNode[er:id/er:permanentId=&quot;42759a40-4664-48ad-830d-
114d0edcf8f7&quot;]/er:node/er:Node" />
          <gml2:directedNode orientation="+"
            xlink:href="#//er:RoadNode[er:id/er:permanentId=&quot;169d9fa8-4de4-466e-9555-
e0b0948c28b6&quot;]/er:node/er:Node" />
          <gml2:curveProperty>
            <gml2:LineString>
              <gml2:coord>
                <gml2:X>123.456</gml2:X>
                <gml2:Y>34.567</gml2:Y>
                <gml2:Z>105</gml2:Z>
              </gml2:coord>
              <gml2:coord>
                <gml2:X>123.456</gml2:X>
                <gml2:Y>123.456</gml2:Y>
                <gml2:Z>100</gml2:Z>
              </gml2:coord>
            </gml2:LineString>
          </gml2:curveProperty>
          <er:roadnetLink xlink:href="#i9" />
        </er:Edge>
      </er:edge>
      <er:formOfWay>SingleCarriageway</er:formOfWay>
      <er:functionalRoadClass>FirstClass</er:functionalRoadClass>
    </er:RoadLink>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:ExcludedManoeuvreFeature>
      <er:id>
        <er:permanentId>2bd2fcbf-6a31-4d5d-bd26-ca7b474e55b0</er:permanentId>
      </er:id>
      <er:location>
        <er:turns>
          <er:node
            xlink:href="#//er:RoadNode[er:id/er:permanentId=&quot;169d9fa8-4de4-466e-9555-
e0b0948c28b6&quot;]" />
          <er:fromElement>
            <er:link
              xlink:href="//er:RoadLink/permanentId[.=&quot;95e6e70a-ff03-4a53-a21e-
8135e0624694&quot;]" />
          </er:fromElement>
          <er:toElement>
            <er:link
              xlink:href="//er:RoadLink/permanentId[.=&quot;7f18a15d-e245-44c8-8bba-
3ae9739a6142&quot;]" />
          </er:toElement>
        </er:turns>
      </er:location>
    </er:ExcludedManoeuvreFeature>
  </gml2:featureMember>
  <gml2:featureMember>
    <er:Route>
      <er:id>
        <er:permanentId>aef110c4-4ae4-4ae4-8dd8-d84bf1188f39</er:permanentId>
      </er:id>
      <er:routeLinks>
        <er:direction>+</er:direction>
        <er:roadnetLink
          xlink:href="//er:RoadLink/permanentId[.=&quot;59ed0f41-96e3-40ed-97a2-
c2ee384e9b11&quot;]" />
      </er:routeLinks>
      <er:routeLinks>
        <er:direction>+</er:direction>
        <er:roadnetLink
          xlink:href="//er:RoadLink/permanentId[.=&quot;95e6e70a-ff03-4a53-a21e-
8135e0624694&quot;]" />
      </er:routeLinks>
    </er:Route>
  </gml2:featureMember>
  <er:properties>
    <er:id>b72ce61a-70b7-4d02-9bb2-1a6d8361be5e</er:id>
    <er:timestamp>
      <gml2:timePosition>2005-12-16 14:07:03</gml2:timePosition>
    </er:timestamp>
  </er:properties>
</er:EuroRoadSDataset>