<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/OnlineTicketingServiceReply/QuerySeatReply/Cinema">
		<xsl:variable name="hallCode" select="Screen/@Code" />
		<Hall>
		   <code><xsl:value-of select="$hallCode"/></code>
		   <seats>
		   <xsl:for-each select="Screen/Seat">
		      <Seat>
		         <code><xsl:value-of select="Code" /></code>
		         <groupCode><xsl:value-of select="GroupCode" /></groupCode>
		         <rowNum><xsl:value-of select="RowNum" /></rowNum>
		         <colNum><xsl:value-of select="ColumnNum" /></colNum>
		         <xCoord><xsl:value-of select="YCoord" /></xCoord>
		         <yCoord><xsl:value-of select="XCoord" /></yCoord>
		         <type>1</type>
		         <status>
		            <xsl:choose>
		               <xsl:when test="Status='Available'">1</xsl:when>
		               <xsl:otherwise>0</xsl:otherwise>
		            </xsl:choose>
		         </status>
		         <hall>
		            <code><xsl:value-of select="$hallCode" /></code>
		         </hall>
		      </Seat>
		   </xsl:for-each>
		   </seats>
		</Hall>
	</xsl:template>
</xsl:stylesheet>