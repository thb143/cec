<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/GetHallSiteResult">
		<GetHallSiteResult>
		  <ResultCode><xsl:value-of select="ResultCode"/></ResultCode>
			<Hall>
			    <seats>
					 <xsl:for-each select="HallSites">
						   <xsl:for-each select="HallSite">
						      	<Seat>
							        <code><xsl:value-of select="SeatNo" /></code>
							        <groupCode><xsl:value-of select="SeatPieceNo" /></groupCode>
							        <rowNum><xsl:value-of select="SeatRow" /></rowNum> 
							        <colNum><xsl:value-of select="SeatCol" /></colNum>
							        <xCoord><xsl:value-of select="GraphRow" /></xCoord>
							        <yCoord><xsl:value-of select="GraphCol" /></yCoord>
							        <type>1</type>
							        <status>1</status>
						      	</Seat>
						   </xsl:for-each>
					</xsl:for-each>
			   </seats>
			</Hall>
		</GetHallSiteResult>
	</xsl:template>
</xsl:stylesheet>